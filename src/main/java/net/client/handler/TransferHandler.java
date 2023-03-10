package net.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.message.Transfer;
import net.proto.SysProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferHandler extends ChannelInboundHandlerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransferHandler.class);
	private final Transfer transfer;

	public TransferHandler(Transfer transfer) {
		this.transfer = transfer;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object o) {
		if (o instanceof SysProto.SysMessage) {
			try {
				if (this.transfer.isTransfer(ctx.channel(), o)) {
					return;
				}
			} catch (Exception var4) {
				LOGGER.error("", var4);
			}
		}

		ctx.fireChannelRead(o);
	}
}
