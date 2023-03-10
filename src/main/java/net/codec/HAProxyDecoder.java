package net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.AttributeKey;

import java.nio.ByteOrder;
import java.util.List;

public class HAProxyDecoder extends ByteToMessageDecoder {
	public static final AttributeKey HAPROXY = AttributeKey.valueOf("haproxy");

	public HAProxyDecoder() {
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
		ByteBuf rec = byteBuf.order(ByteOrder.LITTLE_ENDIAN);
		byte[] recData = new byte[256];
		int i = 0;

		for (int size = recData.length; i < size; ++i) {
			recData[i] = rec.readByte();
			if (10 == recData[i]) {
				recData[i] = 0;
				break;
			}

			if (13 == recData[i]) {
				--i;
			}
		}

		ctx.channel().attr(HAPROXY).setIfAbsent(new String(recData, 0, i));
		ctx.pipeline().remove(this);
		if (byteBuf.writerIndex() > byteBuf.readerIndex()) {
			ctx.fireChannelRead(byteBuf.discardReadBytes());
		}

	}
}
