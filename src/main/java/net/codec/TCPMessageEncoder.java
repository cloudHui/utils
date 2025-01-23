package net.codec;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.message.TCPMessage;


public class TCPMessageEncoder extends MessageToByteEncoder<TCPMessage> {
	public TCPMessageEncoder() {
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, TCPMessage msg, ByteBuf out) {
		ByteBuf buf = out.order(ByteOrder.LITTLE_ENDIAN);
		buf.writeIntLE(msg.getResult());
		buf.writeIntLE(msg.getMessageId());
		int length = msg.getMessage() == null ? 0 : msg.getMessage().length;
		buf.writeIntLE(length);
		buf.writeIntLE(msg.getRoleId());
		buf.writeIntLE(msg.getMapId());
		if (length > 0) {
			buf.writeBytes(msg.getMessage());
		}

	}
}
