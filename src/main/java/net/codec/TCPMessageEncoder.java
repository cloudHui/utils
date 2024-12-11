package net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.message.TCPMessage;


public class TCPMessageEncoder extends MessageToByteEncoder<TCPMessage> {
	public TCPMessageEncoder() {
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, TCPMessage msg, ByteBuf out) {
		out.writeIntLE(msg.getResult());
		out.writeIntLE(msg.getMessageId());
		int length = msg.getMessage() == null ? 0 : msg.getMessage().length;
		out.writeIntLE(length);
		out.writeIntLE(msg.getRoleId());
		out.writeIntLE(msg.getMapId());
		if (length > 0) {
			out.writeBytes(msg.getMessage());
		}

	}
}
