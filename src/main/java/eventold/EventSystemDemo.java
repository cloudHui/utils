package eventold;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// 示例使用
public class EventSystemDemo {

	public static void main(String[] args) {
		// 创建事件分发器（使用自定义线程池）
		ExecutorService executor = Executors.newFixedThreadPool(4);
		EventDispatcher dispatcher = new EventDispatcher(executor);

		// 注册支付事件处理器
		dispatcher.registerHandler(PaymentEvent.class, (EventHandler<PaymentEvent>) event -> {
			System.out.printf("[Payment] User: %s, Amount: %.2f%n", event.getUserId(), event.getAmount());
			// 触发通知事件
			dispatcher.dispatchAsync(new NotificationEvent("Payment processed for user: " + event.getUserId()));
		});

		// 注册通知事件处理器
		dispatcher.registerHandler(NotificationEvent.class, (EventHandler<NotificationEvent>) event -> System.out.println("[Notification] " + event.getMessage()));

		// 模拟事件产生
		dispatcher.dispatchAsync(new PaymentEvent("user123", 99.99));
		dispatcher.dispatchAsync(new PaymentEvent("user456", 149.95));

		// 等待异步处理完成
		try {
			Thread.sleep(1000);
			executor.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}