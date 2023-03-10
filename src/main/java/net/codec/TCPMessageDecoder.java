package net.codec;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import net.message.TCPMessage;

public class TCPMessageDecoder extends LengthFieldBasedFrameDecoder {
	public TCPMessageDecoder() {
		super(ByteOrder.LITTLE_ENDIAN, 2097152, 8, 4, 4, 0, true);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf buf = (ByteBuf) super.decode(ctx, in);
		if (null != buf) {
			ByteBuf rec = buf.order(ByteOrder.LITTLE_ENDIAN);
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

			rec.release();
			return TCPMessage.newInstance(version, id, sequence, data, mapId);
		} else {
			return null;
		}
	}
}
