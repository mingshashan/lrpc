package com.mss.gtr.lrpc.protocol.codec;

import com.mss.gtr.lrpc.protocol.LRpcProtocol;
import com.mss.gtr.lrpc.protocol.MessageHeader;
import com.mss.gtr.lrpc.protocol.serialization.LRpcSerialization;
import com.mss.gtr.lrpc.protocol.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 编码器
 */
public class LRpcEncoder extends MessageToByteEncoder<LRpcProtocol<Object>> {

    private static final Logger logger = LoggerFactory.getLogger(LRpcEncoder.class);


    /**
     * +---------------------------------------------------------------+
     * | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
     * +---------------------------------------------------------------+
     * | 状态 1byte |        消息 ID 8byte     |      数据长度 4byte     |
     * +---------------------------------------------------------------+
     * |                   数据内容 （长度不定）                          |
     * +---------------------------------------------------------------+
     *
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, LRpcProtocol<Object> msg, ByteBuf out) throws Exception {
        MessageHeader header = msg.getMessageHeader();
        out.writeShort(header.getMagic());
        out.writeByte(header.getVersion());
        out.writeByte(header.getSerialization());
        out.writeByte(header.getMsgType());
        out.writeByte(header.getStatus());
        out.writeLong(header.getRequestId());
        LRpcSerialization serialization = SerializationFactory.getSerialization(header.getSerialization());

        byte[] data = serialization.serialize(msg.getBody());
        out.writeInt(data.length);
        out.writeBytes(data);
    }

}
