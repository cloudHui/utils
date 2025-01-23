package net.codec;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import net.message.TCPMessage;

public class TCPMessageDecoder extends LengthFieldBasedFrameDecoder {
	public TCPMessageDecoder() {
		//lengthFieldOffset 长度域的偏移量，简单而言就是偏移几个字节是长度域
		//lengthFieldLength ： 长度域的所占的字节数
		//lengthAdjustment ： 长度值的调整值
		//initialBytesToStrip ： 需要跳过的字节数
		//super(ByteOrder.LITTLE_ENDIAN, 2097152, 8, 4, 4, 0, true);
		//super(ByteOrder.LITTLE_ENDIAN, 2097152, 8, 4, 8, 0, true);
		super(ByteOrder.LITTLE_ENDIAN, 2097152, 8, 4, 8, 0, true);
	}


	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf buf = (ByteBuf) super.decode(ctx, in);
		if (null != buf) {
			try {
				int result = buf.readIntLE();
				int messageId = buf.readIntLE();
				int length = buf.readIntLE();
				int roleId = buf.readIntLE();
				int mapId = buf.readIntLE();
				byte[] data = null;
				if (length > 0) {
					data = new byte[length];
					buf.readBytes(data);//initialBytesToStrip 0
				}

				buf.release();
				return TCPMessage.newInstance(result, messageId, roleId, data, mapId);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		return null;
	}
}
