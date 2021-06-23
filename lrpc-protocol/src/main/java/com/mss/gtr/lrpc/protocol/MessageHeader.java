package com.mss.gtr.lrpc.protocol;

/**
 * 消息头
 * +---------------------------------------------------------------+
 * | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte |
 * +---------------------------------------------------------------+
 * | 状态 1byte |        消息 ID 8byte         |   数据长度 4byte  |
 * +---------------------------------------------------------------+
 * |         数据内容 （长度不定）             |
 * +---------------------------------------------------------------+
 */
public class MessageHeader {
    /**
     * 魔数
     */
    private short magic;

    /**
     * 协议版本号
     */
    private byte version;

    /**
     * 序列化算法
     */
    private byte serialization;

    /**
     * 报文类型
     */
    private byte msgType;

    /**
     * 状态
     */
    private byte status;

    /**
     * 消息ID（请求ID）
     */
    private long requestId;

    /**
     * 消息长度
     * 最大长度 0x7fffffff(2147483647)
     *
     * @see Integer.MAX_VALUE
     */
    private int msgLength;

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public byte getSerialization() {
        return serialization;
    }

    public void setSerialization(byte serialization) {
        this.serialization = serialization;
    }

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public int getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }

    @Override
    public String toString() {
        return "MessageHeader{" +
                "magic=" + magic +
                ", version=" + version +
                ", serialization=" + serialization +
                ", msgType=" + msgType +
                ", status=" + status +
                ", requestId=" + requestId +
                ", msgLength=" + msgLength +
                '}';
    }
}
