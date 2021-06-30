package com.mss.gtr.lrpc.protocol.handler;

import com.mss.gtr.lrpc.core.LRpcException;
import com.mss.gtr.lrpc.core.LRpcRequest;
import com.mss.gtr.lrpc.core.LRpcResponse;
import com.mss.gtr.lrpc.core.util.LRpcServiceHelper;
import com.mss.gtr.lrpc.protocol.LRpcProtocol;
import com.mss.gtr.lrpc.protocol.MessageHeader;
import com.mss.gtr.lrpc.protocol.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 请求处理
 */
public class LRpcRequestHandler extends SimpleChannelInboundHandler<LRpcProtocol<LRpcRequest>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LRpcRequestHandler.class);

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

            Object result = null;
            try {
                result = handleRequest(msg.getBody());
                lRpcResponse.setData(result);
                lRpcResponse.setCode("200");
                lRpcResponse.setMessage("OK");

                header.setStatus((byte) 200);
                protocol.setBody(lRpcResponse);
                protocol.setMessageHeader(header);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                header.setStatus((byte) 500);
                lRpcResponse.setMessage(e.toString());
                LOGGER.error("process request {} error", header.getRequestId(), e);
            }

            ctx.writeAndFlush(protocol);
        });
    }

    private Object handleRequest(LRpcRequest request) throws InvocationTargetException {
        String serviceKey = LRpcServiceHelper.buildServiceKey(request.getClassName(),
                request.getVersion());

        Object serviceBean = rpcServiceMap.get(serviceKey);

        if (null == serviceBean) {
            throw new LRpcException(String.format("service not exist, %s:%s\n",
                    request.getClassName(), request.getVersion()));
        }

        Class<?> clazz = ClassUtils.resolveClassName(request.getClassName(), this.getClass().getClassLoader());
        String methodName = request.getMethodName();
        Class<?>[] parametersType = request.getParameterTypes();
        Object[] parameters = request.getParameters();


        FastClass fastClass = FastClass.create(clazz);
        int methodIndex = fastClass.getIndex(methodName, parametersType);

        Object result = fastClass.invoke(methodIndex, serviceBean, parameters);

        return result;
    }
}
