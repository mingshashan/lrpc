package com.mss.gtr.lrpc.protocol.protocol;

import com.mss.gtr.lrpc.core.LRpcException;
import com.mss.gtr.lrpc.protocol.MessageHeader;

/**
 * 消息类型（请求类型），类型HTTP的POST/GET/PUT
 */
public enum MessageType {

    /**
     * request
     */
    REQUEST(0b00000001),

    /**
     * response
     */
    RESPONSE(0b00000011),

    /**
     * heartbeat
     */
    HEARTBEAT(0b00000111);

    private final int type;

    MessageType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static MessageType getMessageType(int type) {
        for (MessageType messageType : MessageType.values()) {
            if (type == messageType.getType()) {
                return messageType;
            }
        }
        throw new LRpcException("not a required message type, type = [" + type + "].");
    }
}
