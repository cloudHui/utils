package net.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import net.message.TCPMessage;

public class WSTCPMessageDecoder extends MessageToMessageDecoder<WebSocketFrame> {
	public WSTCPMessageDecoder() {
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) {
		ByteBuf rec = msg.content();
		int version = rec.readInt();
		int id = rec.readInt();
		int length = rec.readInt();
		int sequence = rec.readInt();
		int mapId = rec.readInt();
		byte[] data = null;
		if (length > 0) {
			data = new byte[length];
			rec.readBytes(data);
		}

		out.add(TCPMessage.newInstance(version, id, sequence, data, mapId));
	}
}
