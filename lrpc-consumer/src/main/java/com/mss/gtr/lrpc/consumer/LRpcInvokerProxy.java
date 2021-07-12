package com.mss.gtr.lrpc.consumer;

import com.mss.gtr.lrpc.core.LRpcRequest;
import com.mss.gtr.lrpc.core.LRpcRequestHolder;
import com.mss.gtr.lrpc.core.LRpcResponse;
import com.mss.gtr.lrpc.core.util.LRpcFuture;
import com.mss.gtr.lrpc.protocol.LRpcProtocol;
import com.mss.gtr.lrpc.protocol.MessageHeader;
import com.mss.gtr.lrpc.protocol.protocol.MessageType;
import com.mss.gtr.lrpc.protocol.protocol.ProtocolConstant;
import com.mss.gtr.lrpc.protocol.serialization.SerializationFactory;
import com.mss.gtr.lrpc.protocol.serialization.SerializationType;
import com.mss.gtr.lrpc.registry.RegistryService;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class LRpcInvokerProxy implements InvocationHandler {

    private final String serviceVersion;
    private final long timeout;
    private final RegistryService registryService;

    public LRpcInvokerProxy(String serviceVersion, long timeout, RegistryService registryService) {
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.registryService = registryService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        LRpcProtocol<LRpcRequest> lRpcProtocol = new LRpcProtocol<>();
        MessageHeader messageHeader = new MessageHeader();
        long requestId = LRpcRequestHolder.REQUEST_ID_GEN.incrementAndGet();
        messageHeader.setMagic(ProtocolConstant.LRPC_MAGIC);
        messageHeader.setMsgType((byte) MessageType.REQUEST.getType());
        // messageHeader.setSerialization((byte) SerializationType.Hessian.getType());
        messageHeader.setSerialization((byte) SerializationType.JSON.getType());
        messageHeader.setVersion(ProtocolConstant.LRPC_VERSION);
        messageHeader.setStatus(ProtocolConstant.LRPC_STATUS_DEFAULT);
        messageHeader.setRequestId(requestId);

        LRpcRequest lRpcRequest = new LRpcRequest();
        lRpcRequest.setVersion(serviceVersion);
        lRpcRequest.setClassName(method.getDeclaringClass().getName());
        lRpcRequest.setMethodName(method.getName());
        lRpcRequest.setParameterTypes(method.getParameterTypes());
        lRpcRequest.setParameters(args);

        lRpcProtocol.setMessageHeader(messageHeader);
        lRpcProtocol.setBody(lRpcRequest);

        LRpcConsumer lRpcConsumer = new LRpcConsumer();
        LRpcFuture<LRpcResponse> future =

                new LRpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), timeout);

        LRpcRequestHolder.REQUEST_MAP.put(requestId, future);

        lRpcConsumer.sendRequest(lRpcProtocol, registryService);


        Object result = future.getPromise().get(future.getTimeout(), TimeUnit.MILLISECONDS).getData();

        return result;
    }
}

