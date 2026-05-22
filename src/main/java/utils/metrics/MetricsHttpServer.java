package utils.metrics;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 轻量级指标HTTP服务器
 * 使用JDK内置HttpServer，各服务独立暴露/metrics端点
 */
public class MetricsHttpServer {
	private static final Logger logger = LoggerFactory.getLogger(MetricsHttpServer.class);

	private HttpServer server;

	/**
	 * 启动指标HTTP服务器
	 *
	 * @param port 监听端口
	 */
	public void start(int port) {
		try {
			logger.info("正在启动指标HTTP服务器, port: {}", port);
			server = HttpServer.create(new InetSocketAddress(port), 0);
			server.createContext("/metrics", new MetricsHandler());
			server.createContext("/health", new HealthHandler());
			server.setExecutor(null);
			server.start();
			logger.info("指标HTTP服务器启动成功, port: {}, endpoints: [/metrics, /health]", port);
		} catch (IOException e) {
			logger.error("指标HTTP服务器启动失败, port: {}", port, e);
		}
	}

	/**
	 * 停止指标HTTP服务器
	 */
	public void stop() {
		if (server != null) {
			logger.info("正在停止指标HTTP服务器");
			server.stop(1);
			server = null;
			logger.info("指标HTTP服务器已停止");
		} else {
			logger.warn("指标HTTP服务器未运行,跳过停止操作");
		}
	}

	/**
	 * 指标端点处理器
	 */
	private static class MetricsHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			String method = exchange.getRequestMethod();
			String remote = exchange.getRemoteAddress().toString();
			logger.debug("收到指标请求, method: {}, remote: {}", method, remote);

			if (!"GET".equalsIgnoreCase(method)) {
				logger.warn("指标请求方法不允许, method: {}, remote: {}", method, remote);
				sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
				return;
			}

			String json = MetricsCollector.getInstance().toJson();
			exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
			sendResponse(exchange, 200, json);
			logger.debug("指标请求处理完成, remote: {}, responseSize: {}", remote, json.length());
		}
	}

	/**
	 * 健康检查端点处理器
	 */
	private static class HealthHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			String remote = exchange.getRemoteAddress().toString();
			logger.debug("收到健康检查请求, remote: {}", remote);

			String serviceName = MetricsCollector.getInstance().getServiceName();
			String json = "{\"status\":\"UP\",\"service\":\"" + serviceName + "\"}";
			exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
			sendResponse(exchange, 200, json);
			logger.debug("健康检查请求处理完成, remote: {}, service: {}", remote, serviceName);
		}
	}

	private static void sendResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
		byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
		exchange.sendResponseHeaders(statusCode, bytes.length);
		try (OutputStream os = exchange.getResponseBody()) {
			os.write(bytes);
		}
		logger.trace("发送HTTP响应, statusCode: {}, size: {}", statusCode, bytes.length);
	}
}
