package net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import net.proto.SysProto;

import java.util.List;

public class WebSocketWrapEncoder extends MessageToMessageEncoder<SysProto.SysMessage> {
	public WebSocketWrapEncoder() {
	}

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, SysProto.SysMessage sysMessage, List<Object> list) throws Exception {
		ByteBuf buf = Unpooled.wrappedBuffer(sysMessage.toByteArray());
		list.add(new BinaryWebSocketFrame(buf));
	}
}
