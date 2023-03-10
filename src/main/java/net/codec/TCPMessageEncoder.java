package net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.message.TCPMessage;

import java.nio.ByteOrder;

public class TCPMessageEncoder extends MessageToByteEncoder<TCPMessage> {
	public TCPMessageEncoder() {
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, TCPMessage msg, ByteBuf out) throws Exception {
		ByteBuf buf = out.order(ByteOrder.LITTLE_ENDIAN);
		buf.writeInt(msg.getVersion());
		buf.writeInt(msg.getMessageId());
		int length = msg.getMessage() == null ? 0 : msg.getMessage().length;
		buf.writeInt(length);
		buf.writeInt(msg.getSequence());
		if (length > 0) {
			buf.writeBytes(msg.getMessage());
		}

	}
}
