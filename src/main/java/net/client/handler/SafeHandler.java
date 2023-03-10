package net.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.proto.SysProto;
import net.safe.Safe;

public class SafeHandler extends ChannelInboundHandlerAdapter {
	private final Safe safe;

	public SafeHandler(Safe safe) {
		this.safe = safe;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object o) throws Exception {
		if (o instanceof SysProto.SysMessage && !this.safe.isValid(ctx.channel(), o)) {
			ctx.close();
		} else {
			ctx.fireChannelRead(o);
		}
	}
}
