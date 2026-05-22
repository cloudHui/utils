package utils.trace;

import java.util.Map;

import com.google.protobuf.Message;
import net.client.Sender;
import net.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 带链路追踪的Handler包装器
 * 在handler执行前后自动管理TraceContext生命周期
 */
public class TracedHandler implements Handler {
	private static final Logger logger = LoggerFactory.getLogger(TracedHandler.class);

	private final Handler delegate;
	private final int msgId;

	public TracedHandler(Handler delegate, int msgId) {
		this.delegate = delegate;
		this.msgId = msgId;
		logger.debug("创建TracedHandler, msgId: 0x{}, delegate: {}",
				Integer.toHexString(msgId), delegate.getClass().getSimpleName());
	}

	@Override
	public boolean handler(Sender sender, int clientId, Message msg, long mapId, int sequence) {
		String traceId = TraceContext.beginTrace();
		long start = System.currentTimeMillis();
		try {
			logger.debug("消息处理开始, msgId: 0x{}, clientId: {}, mapId: {}, seq: {}, traceId: {}",
					Integer.toHexString(msgId), clientId, mapId, sequence, traceId);
			boolean result = delegate.handler(sender, clientId, msg, mapId, sequence);
			long cost = System.currentTimeMillis() - start;
			if (cost > 500) {
				logger.warn("消息处理慢, msgId: 0x{}, clientId: {}, traceId: {}, cost: {}ms",
						Integer.toHexString(msgId), clientId, traceId, cost);
			} else {
				logger.debug("消息处理完成, msgId: 0x{}, clientId: {}, traceId: {}, cost: {}ms",
						Integer.toHexString(msgId), clientId, traceId, cost);
			}
			return result;
		} catch (Exception e) {
			long cost = System.currentTimeMillis() - start;
			logger.error("消息处理异常, msgId: 0x{}, clientId: {}, traceId: {}, cost: {}ms",
					Integer.toHexString(msgId), clientId, traceId, cost, e);
			throw e;
		} finally {
			TraceContext.endTrace();
		}
	}

	/**
	 * 包装Map中所有Handler为带追踪的Handler
	 * 在ClientProto.init()中注册完处理器后调用
	 */
	public static void wrapAll(Map<Integer, Handler> handlerMap) {
		int wrapped = 0;
		int skipped = 0;
		for (Map.Entry<Integer, Handler> entry : handlerMap.entrySet()) {
			Handler original = entry.getValue();
			if (original instanceof TracedHandler) {
				skipped++;
				continue;
			}
			entry.setValue(new TracedHandler(original, entry.getKey()));
			wrapped++;
		}
		logger.info("Handler链路追踪包装完成, 总数: {}, 新包装: {}, 已跳过: {}",
				handlerMap.size(), wrapped, skipped);
	}
}
