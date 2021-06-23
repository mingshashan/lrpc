package com.mss.gtr.lrpc.protocol.serialization;

import com.mss.gtr.lrpc.core.LRpcException;

public enum SerializationType {

    Hessian(0b0000001),

    JSON(0b0000010);

    private int type;

    SerializationType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static SerializationType getByType(byte type) {
        for (SerializationType serializationType : SerializationType.values()) {
            if (serializationType.type == (Integer.MAX_VALUE & type)) {
                return serializationType;
            }
        }

        throw new LRpcException("serialization type no exist, type = [" + type + "].");
    }

}
