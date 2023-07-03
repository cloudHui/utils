package net.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@ChannelHandler.Sharable
public class IdleHandler extends ChannelInboundHandlerAdapter {


	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object paramObject) throws Exception {
		if (paramObject instanceof IdleStateEvent) {
			IdleState state = ((IdleStateEvent) paramObject).state();
			if (state == IdleState.ALL_IDLE) {
				//关闭连接
				ctx.channel().close();
			}
		} else {
			super.userEventTriggered(ctx, paramObject);
		}
	}
}
