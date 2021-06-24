package com.mss.gtr.lrpc.protocol.serialization;

import java.io.IOException;

/**
 * protobuf序列化
 */
public class ProtobufSerialization implements LRpcSerialization {
    @Override
    public <T> byte[] serialize(T data) throws IOException {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> type) throws IOException {
        return null;
    }
}
