package net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import net.proto.SysProto;

import java.util.List;

public class WSSysMessageEncoder extends MessageToMessageEncoder<SysProto.SysMessage> {
	public WSSysMessageEncoder() {
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, SysProto.SysMessage msg, List<Object> out) {
		ByteBuf buf = Unpooled.wrappedBuffer(msg.toByteArray());
		out.add(new BinaryWebSocketFrame(buf));
	}
}
