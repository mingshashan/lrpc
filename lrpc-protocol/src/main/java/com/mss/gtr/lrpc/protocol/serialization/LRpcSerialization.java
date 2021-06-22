package com.mss.gtr.lrpc.protocol.serialization;

import java.io.IOException;

public interface LRpcSerialization {

    /**
     * 序列化数据
     *
     * @param data 数据
     * @param <T>  泛型参数
     * @return 序列化后的字节数组
     * @throws IOException io异常
     */
    <T> byte[] serialize(T data) throws IOException;

    /**
     * 反序列化
     *
     * @param data 字节数据
     * @param type 类型
     * @param <T>  泛型参数
     * @return 反序列化后的数据
     * @throws IOException io异常
     */
    <T> T deserialize(byte[] data, Class<T> type) throws IOException;
}
