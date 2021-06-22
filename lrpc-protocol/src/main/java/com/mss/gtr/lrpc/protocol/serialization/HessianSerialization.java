package com.mss.gtr.lrpc.protocol.serialization;

import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.SerializerFactory;
import com.mss.gtr.lrpc.core.util.Assert;

import java.io.IOException;

public class HessianSerialization implements LRpcSerialization {
    AbstractSerializerFactory serializerFactory = new SerializerFactory();

    @Override
    public <T> byte[] serialize(T data) throws IOException {
        Assert.notNull(data, "the serialize data must not be null");



//        AbstractHessianOutput output =
//        serializerFactory.getSerializer(ArraySerializer.class)
//                .writeObject(data, );
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> type) throws IOException {
        return null;
    }
}
