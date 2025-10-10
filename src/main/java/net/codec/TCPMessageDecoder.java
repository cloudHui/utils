package net.codec;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import net.message.TCPMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TCPMessageDecoder extends LengthFieldBasedFrameDecoder {
	private static final Logger logger = LoggerFactory.getLogger(TCPMessageDecoder.class);

	public TCPMessageDecoder() {
		// 参数说明：
		// maxFrameLength: 最大帧长度 2097152 (2MB)
		// lengthFieldOffset: 长度域偏移量 8字节 (跳过result 4 + messageId 4)
		// lengthFieldLength: 长度域长度 4字节
		// lengthAdjustment: 长度调整值 16 (需要包含clientId 4 + mapId 4 + sequence 8)
		// initialBytesToStrip: 跳过的字节数 16 (跳过整个头部)
		// failFast: 快速失败 true
		super(ByteOrder.LITTLE_ENDIAN, 2097152, 8, 4, 16, 0, true);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if (frame == null) {
			return null;
		}

		try {
			return decodeTCPMessage(frame);
		} catch (Exception e) {
			logger.error("TCP消息解码错误", e);
			return null;
		} finally {
			frame.release();
		}
	}

	/**
	 * 解码TCP消息
	 * 消息格式: [result 4][messageId 4][length 4][clientId 4][mapId 4][sequence 8][data ?]
	 */
	private TCPMessage decodeTCPMessage(ByteBuf buf) {
		// 验证可读字节数是否足够
		if (buf.readableBytes() < 28) { // 头部固定28字节
			logger.error("消息长度不足，期望至少28字节，实际:{}字节", buf.readableBytes());
			return null;
		}

		int result = buf.readIntLE();
		int messageId = buf.readIntLE();
		int length = buf.readIntLE(); // 数据部分长度

		// 验证数据长度是否合理
		if (length < 0 || length > 2097152 - 28) {
			logger.error("数据长度异常: {} (最大允许: {})", length, 2097152 - 28);
			return null;
		}

		// 验证剩余字节数是否足够
		if (buf.readableBytes() < 16 + length) { // clientId 4 + mapId 4 + sequence 8 + data length
			logger.error("数据不完整，期望:{}字节，实际:{}字节", 16 + length, buf.readableBytes());
			return null;
		}

		int clientId = buf.readIntLE();
		int mapId = buf.readIntLE();
		long sequence = buf.readLongLE();

		// 读取数据部分
		byte[] data = null;
		if (length > 0) {
			data = new byte[length];
			buf.readBytes(data);

			// 验证是否读取了正确数量的字节
		}

		logger.debug("解码消息: result={}, messageId=0x{}, clientId={}, mapId={}, sequence={}, dataLen={}",
				result, Integer.toHexString(messageId), clientId, mapId, sequence, length);

		return TCPMessage.newInstance(result, messageId, clientId, data, mapId, sequence);
	}
}