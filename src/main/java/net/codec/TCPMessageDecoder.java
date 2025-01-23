package net.codec;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import net.client.handler.ClientHandler;
import net.message.TCPMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TCPMessageDecoder extends LengthFieldBasedFrameDecoder {
	private static final Logger logger = LoggerFactory.getLogger(TCPMessageDecoder.class);
	public TCPMessageDecoder() {
		//lengthFieldOffset 长度域的偏移量，简单而言就是偏移几个字节是长度域
		//lengthFieldLength ： 长度域的所占的字节数
		//lengthAdjustment ： 长度值的调整值
		//initialBytesToStrip ： 需要跳过的字节数
		//super(ByteOrder.LITTLE_ENDIAN, 2097152, 8, 4, 4, 0, true);
		//super(ByteOrder.LITTLE_ENDIAN, 2097152, 8, 4, 8, 0, true);
		//super(ByteOrder.LITTLE_ENDIAN, 2097152, 8, 4, 16, 0, true);
		super(ByteOrder.LITTLE_ENDIAN, 2097152, 8, 4, 16, 0, true);
	}


	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf buf = (ByteBuf) super.decode(ctx, in);
		if (null != buf) {
			try {
				int result = buf.readIntLE();
				int messageId = buf.readIntLE();
				int length = buf.readIntLE();//lengthAdjustment 后面需要跳过的字节  clientId 4 mapId 4 sequence 8
				int clientId = buf.readIntLE();
				int mapId = buf.readIntLE();
				long sequence = buf.readLongLE();
				byte[] data = null;
				if (length > 0) {
					data = new byte[length];
					buf.readBytes(data);//initialBytesToStrip 0
				}

				buf.release();
				return TCPMessage.newInstance(result, messageId, clientId, data, mapId, sequence);
			} catch (Exception e) {
				logger.error("decode error",e);
				throw e;
			}
		}
		return null;
	}
}
