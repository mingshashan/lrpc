package com.mss.gtr.lrpc.protocol;

import java.io.Serializable;

/**
 * LRPC自定义协议
 *
 * @param <T>
 */
public class LRpcProtocol<T> implements Serializable {

    /**
     * 消息头
     */
    private MessageHeader messageHeader;

    /**
     * 消息体
     */
    private T body;

}

