package eventold;

/**
 * @author admin
 * @className EventHandler
 * @description
 * @createDate 2025/7/30 8:07
 */ // 事件处理器接口
@FunctionalInterface
interface EventHandler<T extends Event> {
	void handle(T event);
}
