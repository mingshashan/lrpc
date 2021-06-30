package com.mss.gtr.lrpc.consumer;

import com.mss.gtr.lrpc.core.LRpcRequest;
import com.mss.gtr.lrpc.core.ServiceMeta;
import com.mss.gtr.lrpc.core.util.LRpcServiceHelper;
import com.mss.gtr.lrpc.protocol.LRpcProtocol;
import com.mss.gtr.lrpc.protocol.codec.LRpcDecoder;
import com.mss.gtr.lrpc.protocol.codec.LRpcEncoder;
import com.mss.gtr.lrpc.protocol.handler.LRpcResponseHandler;
import com.mss.gtr.lrpc.registry.RegistryService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LRpcConsumer {
    private final static Logger LOGGER = LoggerFactory.getLogger(LRpcConsumer.class);

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public LRpcConsumer() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LRpcEncoder())
                                .addLast(new LRpcDecoder())
                                .addLast(new LRpcResponseHandler());
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

    }

    public void sendRequest(
            LRpcProtocol<LRpcRequest> protocol, RegistryService registryService) throws Exception {
        LRpcRequest lRpcRequest = protocol.getBody();
        Object[] parameters = lRpcRequest.getParameters();
        String serviceKey = LRpcServiceHelper
                .buildServiceKey(lRpcRequest.getClassName(), lRpcRequest
                        .getVersion());

        int invokeHashCode = parameters.length > 0 ?
                parameters[0].hashCode() : serviceKey.hashCode();

        // 从注册中心查找服务
        ServiceMeta serviceMeta = registryService
                .discovery(serviceKey, invokeHashCode);
        if (null != serviceMeta) {
            // netty client创建连接
            ChannelFuture channelFuture =
                    bootstrap.connect(
                            serviceMeta.getServiceAddr(), serviceMeta.getServicePort())
                            .sync();

            // 添加监听
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    // 成功
                    LOGGER.info("lrpc connect server [{}:{}] success.",
                            serviceMeta.getServiceAddr(),
                            serviceMeta.getServiceAddr());
                } else {
                    // 失败
                    LOGGER.error("lrpc connect server [{}:{}] error.",
                            serviceMeta.getServiceAddr(),
                            serviceMeta.getServiceAddr());
                    future.cause().printStackTrace();
                    eventLoopGroup.shutdownGracefully();
                }
            });

            // 发送请求
            channelFuture.channel().writeAndFlush(protocol);
        }
    }

}
