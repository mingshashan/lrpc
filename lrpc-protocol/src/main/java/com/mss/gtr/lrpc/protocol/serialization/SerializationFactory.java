package com.mss.gtr.lrpc.protocol.serialization;

import com.mss.gtr.lrpc.core.LRpcException;

/**
 * 序列化协议工厂
 */
public class SerializationFactory {

    public static LRpcSerialization getSerialization(byte iType) {

        SerializationType serializationType = SerializationType.getByType(iType);

        switch (serializationType) {
            case Hessian:
                return new HessianSerialization();
            case JSON:
                return new JsonSerialization();
            default:
                throw new SerializationException("there is not serialization for [" + serializationType + "].");
        }

    }

}
