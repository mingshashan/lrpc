package com.mss.gtr.lrpc.protocol.protocol;

/**
 * 协议常量
 */
public interface ProtocolConstant {

    /**
     * 消息头长度
     */
    int LRPC_HEADER_LENGTH = 18;

    /**
     * 消息魔数
     */
    short LRPC_MAGIC = 0x6A6B;

    /**
     * 版本
     */
    byte LRPC_VERSION = 0x01;
}
