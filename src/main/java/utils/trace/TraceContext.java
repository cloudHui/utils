package utils.trace;

import java.util.UUID;

import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 链路上下文
 * 基于ThreadLocal + SLF4J MDC实现请求级traceId贯穿
 * traceId在消息处理开始时生成，处理结束时清除
 */
public class TraceContext {
	private static final Logger logger = LoggerFactory.getLogger(TraceContext.class);

	private static final String TRACE_KEY = "traceId";
	private static final String USER_KEY = "userId";
	private static final String TABLE_KEY = "tableId";

	private static final ThreadLocal<String> traceIdHolder = new ThreadLocal<>();

	/**
	 * 生成并设置traceId
	 */
	public static String beginTrace() {
		String traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
		traceIdHolder.set(traceId);
		MDC.put(TRACE_KEY, traceId);
		logger.debug("链路追踪开始, traceId: {}", traceId);
		return traceId;
	}

	/**
	 * 使用已有traceId（跨服务传递时使用）
	 */
	public static void setTraceId(String traceId) {
		String old = traceIdHolder.get();
		traceIdHolder.set(traceId);
		MDC.put(TRACE_KEY, traceId);
		logger.debug("设置链路traceId, old: {}, new: {}", old, traceId);
	}

	/**
	 * 获取当前traceId
	 */
	public static String getTraceId() {
		String traceId = traceIdHolder.get();
		logger.trace("获取traceId: {}", traceId);
		return traceId;
	}

	/**
	 * 设置userId到MDC（日志中可看到是哪个用户）
	 */
	public static void setUserId(int userId) {
		MDC.put(USER_KEY, String.valueOf(userId));
		logger.debug("设置链路userId: {}", userId);
	}

	/**
	 * 设置tableId到MDC（日志中可看到是哪张桌子）
	 */
	public static void setTableId(long tableId) {
		MDC.put(TABLE_KEY, String.valueOf(tableId));
		logger.debug("设置链路tableId: {}", tableId);
	}

	/**
	 * 清除上下文（消息处理结束时调用）
	 */
	public static void endTrace() {
		String traceId = traceIdHolder.get();
		traceIdHolder.remove();
		MDC.remove(TRACE_KEY);
		MDC.remove(USER_KEY);
		MDC.remove(TABLE_KEY);
		logger.debug("链路追踪结束, traceId: {}", traceId);
	}
}
