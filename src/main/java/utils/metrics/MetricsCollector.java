package utils.metrics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 指标收集器
 * 各服务注册自己的指标数据，支持HTTP端点查询
 */
public class MetricsCollector {
	private static final Logger logger = LoggerFactory.getLogger(MetricsCollector.class);

	private static final MetricsCollector INSTANCE = new MetricsCollector();

	/** 服务名称 */
	private String serviceName;
	/** 计数器指标 name -> value */
	private final ConcurrentHashMap<String, AtomicLong> counters = new ConcurrentHashMap<>();
	/** 仪表盘指标 name -> value */
	private final ConcurrentHashMap<String, AtomicLong> gauges = new ConcurrentHashMap<>();

	private MetricsCollector() {
		logger.info("指标收集器初始化完成");
	}

	public static MetricsCollector getInstance() {
		return INSTANCE;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
		logger.info("设置指标服务名称: {}", serviceName);
	}

	public String getServiceName() {
		logger.trace("获取服务名称: {}", serviceName);
		return serviceName;
	}

	/**
	 * 增加计数器
	 */
	public void incrementCounter(String name) {
		long val = counters.computeIfAbsent(name, k -> new AtomicLong()).incrementAndGet();
		logger.trace("计数器递增, name: {}, value: {}", name, val);
	}

	/**
	 * 增加计数器指定值
	 */
	public void incrementCounter(String name, long delta) {
		long val = counters.computeIfAbsent(name, k -> new AtomicLong()).addAndGet(delta);
		logger.debug("计数器递增, name: {}, delta: {}, value: {}", name, delta, val);
	}

	/**
	 * 设置仪表盘指标
	 */
	public void setGauge(String name, long value) {
		gauges.computeIfAbsent(name, k -> new AtomicLong()).set(value);
		logger.debug("设置仪表盘指标, name: {}, value: {}", name, value);
	}

	/**
	 * 获取仪表盘指标
	 */
	public long getGauge(String name) {
		AtomicLong val = gauges.get(name);
		long result = val != null ? val.get() : 0;
		logger.trace("获取仪表盘指标, name: {}, value: {}", name, result);
		return result;
	}

	/**
	 * 获取计数器指标
	 */
	public long getCounter(String name) {
		AtomicLong val = counters.get(name);
		long result = val != null ? val.get() : 0;
		logger.trace("获取计数器指标, name: {}, value: {}", name, result);
		return result;
	}

	/**
	 * 导出所有指标为JSON字符串
	 */
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"service\":\"").append(serviceName != null ? serviceName : "unknown").append("\",");
		sb.append("\"timestamp\":").append(System.currentTimeMillis()).append(",");

		sb.append("\"gauges\":{");
		boolean first = true;
		for (Map.Entry<String, AtomicLong> entry : gauges.entrySet()) {
			if (!first) sb.append(",");
			sb.append("\"").append(entry.getKey()).append("\":").append(entry.getValue().get());
			first = false;
		}
		sb.append("},");

		sb.append("\"counters\":{");
		first = true;
		for (Map.Entry<String, AtomicLong> entry : counters.entrySet()) {
			if (!first) sb.append(",");
			sb.append("\"").append(entry.getKey()).append("\":").append(entry.getValue().get());
			first = false;
		}
		sb.append("}}");

		String json = sb.toString();
		logger.debug("导出指标JSON, gauges: {}, counters: {}, length: {}",
				gauges.size(), counters.size(), json.length());
		return json;
	}
}
