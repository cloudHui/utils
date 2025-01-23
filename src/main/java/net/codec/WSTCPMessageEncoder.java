package net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import net.message.TCPMessage;

import java.util.List;

public class WSTCPMessageEncoder extends MessageToMessageEncoder<TCPMessage> {
	public WSTCPMessageEncoder() {
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, TCPMessage msg, List<Object> out) {
		int length = msg.getMessage() == null ? 0 : msg.getMessage().length;
		ByteBuf buf = Unpooled.buffer(length + 40);
		buf.writeInt(msg.getResult());
		buf.writeInt(msg.getMessageId());
		buf.writeInt(length);
		buf.writeInt(msg.getClientId());
		if (length > 0) {
			buf.writeBytes(msg.getMessage());
		}

		out.add(new BinaryWebSocketFrame(buf));
	}
}
