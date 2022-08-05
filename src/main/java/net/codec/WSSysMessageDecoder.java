package net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import net.proto.SysProto;

import java.util.List;

public class WSSysMessageDecoder extends MessageToMessageDecoder<WebSocketFrame> {
	public WSSysMessageDecoder() {
	}

	protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
		ByteBuf buf = msg.content();
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		out.add(SysProto.SysMessage.parseFrom(bytes));
	}
}
