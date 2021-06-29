package com.mss.gtr.lrpc.provider;

import com.mss.gtr.lrpc.core.LRpcException;
import com.mss.gtr.lrpc.core.ServiceMeta;
import com.mss.gtr.lrpc.core.annotation.RpcService;
import com.mss.gtr.lrpc.protocol.codec.LRpcDecoder;
import com.mss.gtr.lrpc.protocol.codec.LRpcEncoder;
import com.mss.gtr.lrpc.protocol.handler.LRpcRequestHandler;
import com.mss.gtr.lrpc.registry.RegistryService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


public class LRpcProvider implements InitializingBean, BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LRpcProvider.class);

    private String serviceAddr;
    private final int port;
    private final RegistryService registryService;

    private final Map<String, Object> rpcServiceMap = new HashMap<>();

    public LRpcProvider(int port, RegistryService registryService) {
        this.port = port;
        this.registryService = registryService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            try {
                startRpcServer();
            } catch (Exception e) {
                LOGGER.error("start rpc server error.");
                throw new LRpcException(e);
            }

        }).start();
    }

    private void startRpcServer() throws UnknownHostException {
        this.serviceAddr = InetAddress.getLocalHost().getHostAddress();

        EventLoopGroup boss = new NioEventLoopGroup(2);
        EventLoopGroup worker = new NioEventLoopGroup(2);

        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LRpcEncoder())
                                .addLast(new LRpcDecoder())
                                .addLast(new LRpcRequestHandler(rpcServiceMap));
                    }
                }).childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true);

        try {
            ChannelFuture future = serverBootstrap.bind(this.serviceAddr, this.port).sync();

            LOGGER.info("the rpc server started, {}:{}", serviceAddr, port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
        if (null != rpcService) {
            String serviceName = rpcService.serviceInterface().getName();
            String serviceVersion = rpcService.serviceVersion();

            ServiceMeta serviceMeta = new ServiceMeta();
            serviceMeta.setServiceAddr(serviceAddr);
            serviceMeta.setServicePort(port);
            serviceMeta.setServiceName(serviceName);
            serviceMeta.setServiceVersion(serviceVersion);

            try {
                registryService.registry(serviceMeta);
            } catch (Exception e) {
                LOGGER.error("failed to register service {}#{}", serviceName, serviceVersion, e);
                throw new LRpcException(e);
            }
        }

        return bean;
    }
}
