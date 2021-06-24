package com.mss.gtr.lrpc.protocol.handler;

import com.mss.gtr.lrpc.core.LRpcRequestHolder;
import com.mss.gtr.lrpc.core.LRpcResponse;
import com.mss.gtr.lrpc.core.util.LRpcFuture;
import com.mss.gtr.lrpc.protocol.LRpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LRpcResponseHandler extends SimpleChannelInboundHandler<LRpcProtocol<LRpcResponse>> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LRpcProtocol<LRpcResponse> msg) throws Exception {

        long requestId = msg.getMessageHeader().getRequestId();

        LRpcFuture<LRpcResponse> future = LRpcRequestHolder.REQUEST_MAP
                .remove(requestId);

        future.getPromise().setSuccess(msg.getBody()    );

    }
}
