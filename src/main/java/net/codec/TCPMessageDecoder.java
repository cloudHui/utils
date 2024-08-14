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
		super(ByteOrder.LITTLE_ENDIAN, 2097152, 8, 4,
				8, 0, true);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf buf = (ByteBuf) super.decode(ctx, in);
		if (null != buf) {
			ByteBuf rec = buf.order(ByteOrder.LITTLE_ENDIAN);
			int result = rec.readInt();//lengthFieldOffset 4
			int id = rec.readInt();//lengthFieldOffset total 8
			int length = rec.readInt();//lengthFieldLength 4
			int sequence = rec.readInt();//lengthAdjustment 4
			int mapId = rec.readInt();  //lengthAdjustment total 8
			byte[] data = null;
			if (length > 0) {
				data = new byte[length];
				rec.readBytes(data);//initialBytesToStrip 0
			}

			rec.release();
			return TCPMessage.newInstance(result, id, sequence, data, mapId);
		} else {
			return null;
		}
	}
}
