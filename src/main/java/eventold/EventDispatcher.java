package eventold;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

// 事件分发器
public class EventDispatcher {
	private final Map<Class, List<EventHandler<? extends Event>>> handlers = new ConcurrentHashMap<>();
	private final Executor executor;

	public EventDispatcher() {
		this.executor = ForkJoinPool.commonPool(); // 使用公共线程池
	}

	public EventDispatcher(Executor executor) {
		this.executor = executor;
	}

	// 注册事件处理器
	public <T extends Event> void registerHandler(Class cls, EventHandler<T> handler) {
		handlers.computeIfAbsent(cls, k -> new CopyOnWriteArrayList<>()).add(handler);
	}

	// 分发事件（异步）
	public <T extends Event> void dispatchAsync(T event) {
		List<EventHandler<? extends Event>> eventHandlers = handlers.get(event.getClass());
		if (eventHandlers != null) {
			for (EventHandler handler : eventHandlers) {
				executor.execute(() -> {
					try {
						handler.handle(event);
					} catch (Exception e) {
						System.err.println("Error handling event: " + event.getType());
						e.printStackTrace();
					}
				});
			}
		}
	}

	// 分发事件（同步）
	public <T extends Event> void dispatchSync(T event) {
		List<EventHandler<? extends Event>> eventHandlers = handlers.get(event.getType());
		if (eventHandlers != null) {
			for (EventHandler handler : eventHandlers) {
				try {
					handler.handle(event);
				} catch (Exception e) {
					System.err.println("Error handling event: " + event.getType());
					e.printStackTrace();
				}
			}
		}
	}
}