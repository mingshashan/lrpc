package com.mss.gtr.lrpc.protocol.codec;

import com.mss.gtr.lrpc.core.LRpcException;
import com.mss.gtr.lrpc.protocol.MessageHeader;
import com.mss.gtr.lrpc.protocol.protocol.MessageType;
import com.mss.gtr.lrpc.protocol.protocol.ProtocolConstant;
import com.mss.gtr.lrpc.protocol.serialization.LRpcSerialization;
import com.mss.gtr.lrpc.protocol.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 编码器
 */
public class LRpcEncoder extends ByteToMessageDecoder {

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
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if (in.readableBytes() < ProtocolConstant.LRPC_HEADER_LENGTH) {
            logger.warn("the receive message is not a complete message");
            return;
        }

        in.markReaderIndex();

        short magic = in.readShort();
        if (ProtocolConstant.LRPC_MAGIC != magic) {
            throw new LRpcException("magic number is illegal, " + magic);
        }

        byte version = in.readByte();
        byte serialization = in.readByte();
        byte msgType = in.readByte();
        byte status = in.readByte();
        long requestId = in.readLong();
        int msgLength = in.readInt();

        if (in.readableBytes() < msgLength) {
            logger.warn("the message is not complete.");

            // 设置readIndex为读之前mark的位置
            in.resetReaderIndex();
            return;
        }

        byte[] data = new byte[msgLength];
        in.readBytes(data);

        MessageType messageType = MessageType.getMessageType(msgType);

        MessageHeader header = new MessageHeader();
        header.setMagic(magic);
        header.setVersion(version);
        header.setSerialization(serialization);
        header.setMsgType(msgType);
        header.setStatus(status);
        header.setRequestId(requestId);
        header.setMsgLength(msgLength);

        LRpcSerialization lRpcSerialization = SerializationFactory.getSerialization(serialization);


    }
}
