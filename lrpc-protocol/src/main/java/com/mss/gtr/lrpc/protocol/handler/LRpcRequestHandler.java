package com.mss.gtr.lrpc.protocol.handler;

import com.mss.gtr.lrpc.core.LRpcRequest;
import com.mss.gtr.lrpc.core.LRpcResponse;
import com.mss.gtr.lrpc.core.util.LRpcServiceHelper;
import com.mss.gtr.lrpc.protocol.LRpcProtocol;
import com.mss.gtr.lrpc.protocol.MessageHeader;
import com.mss.gtr.lrpc.protocol.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

/**
 * 请求处理
 */
public class LRpcRequestHandler extends SimpleChannelInboundHandler<LRpcProtocol<LRpcRequest>> {

    private final Map<String, Object> rpcServiceMap;

    public LRpcRequestHandler(Map<String, Object> rpcServiceMap) {
        this.rpcServiceMap = rpcServiceMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LRpcProtocol<LRpcRequest> msg) throws Exception {

        LRpcRequestProcessor.submitRequest(() -> {
            LRpcProtocol<LRpcResponse> protocol = new LRpcProtocol<>();
            LRpcResponse lRpcResponse = new LRpcResponse();
            MessageHeader header = msg.getMessageHeader();
            header.setMsgType((byte) MessageType.RESPONSE.getType());

            Object result = handleRequest(msg.getBody());
            lRpcResponse.setData(result);
            lRpcResponse.setCode("200");
            lRpcResponse.setMessage("OK");

            MessageHeader responseHeader = new MessageHeader();
            responseHeader.setStatus((byte) 200);
            protocol.setBody(lRpcResponse);
            protocol.setMessageHeader(responseHeader);
            ctx.writeAndFlush(protocol);
        });
    }

    private Object handleRequest(LRpcRequest body) {
//        String serviceKey = LRpcServiceHelper.buildServiceKey()
        return null;
    }
}
