package threadtutil.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import threadtutil.utils.TimeUtils;

/**
 * 自定义线程池实现类
 * 提供固定大小的线程池，并支持线程命名和管理功能
 */
public class ThreadPool {
	// 线程池执行器
	private final ExecutorService pools;
	// 线程组
	private final ThreadGroup group;
	// 线程名称前缀
	private final String threadPrefix;
	// 线程编号原子计数器
	private final AtomicInteger threadNumber;
	// 线程ID与名称映射关系
	private final Map<Long, String> threadName;
	// 线程池大小
	private final int size;

	/**
	 * 构造函数，使用默认大小创建线程池
	 *
	 * @param prefix 线程名称前缀
	 */
	public ThreadPool(String prefix) {
		this(prefix, 0);
	}

	/**
	 * 构造函数，指定大小创建线程池
	 *
	 * @param prefix 线程名称前缀
	 * @param size   线程池大小
	 */
	public ThreadPool(String prefix, int size) {
		this.threadName = new HashMap<>(size);
		SecurityManager s = System.getSecurityManager();
		// 获取当前线程组
		this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		this.threadPrefix = prefix;
		this.threadNumber = new AtomicInteger(0);
		// 设置线程池大小，默认使用CPU核心数
		this.size = (size > 0) ? size : TimeUtils.PROCESS_NUMBER;

		// 创建线程池执行器
		this.pools = new ThreadPoolExecutor(
				this.size,
				this.size,
				0L,
				TimeUnit.SECONDS,
				new LinkedBlockingDeque<>(1000),
				this::createThread
		);
	}

	/**
	 * 创建新线程的工厂方法
	 *
	 * @param runnable 线程要执行的任务
	 * @return 新创建的线程
	 */
	private Thread createThread(Runnable runnable) {
		Thread t = new Thread(this.group, runnable,
				this.threadPrefix + "_" + this.threadNumber.incrementAndGet(), 0L);

		// 设置为非守护线程
		if (t.isDaemon()) {
			t.setDaemon(false);
		}

		// 设置线程优先级为默认值
		if (t.getPriority() != Thread.NORM_PRIORITY) {
			t.setPriority(Thread.NORM_PRIORITY);
		}

		// 记录线程ID与名称的映射关系
		this.threadName.put(t.getId(), t.getName());
		return t;
	}

	/**
	 * 获取线程池大小
	 *
	 * @return 线程池大小
	 */
	public final int size() {
		return this.size;
	}

	/**
	 * 获取线程池执行器
	 *
	 * @return ExecutorService实例
	 */
	public ExecutorService getPools() {
		return this.pools;
	}

	/**
	 * 执行任务
	 *
	 * @param runnable 可运行任务
	 * @return Future对象，可用于获取执行结果
	 */
	public Future<?> execute(Runnable runnable) {
		return this.pools.submit(runnable);
	}

	/**
	 * 关闭线程池
	 */
	public void close() {
		this.pools.shutdown();
	}

	/**
	 * 根据线程ID获取线程名称
	 *
	 * @param threadId 线程ID
	 * @return 线程名称
	 */
	protected String getThreadName(long threadId) {
		return this.threadName.get(threadId);
	}

	/**
	 * 根据线程ID查找线程对象
	 *
	 * @param threadId 线程ID
	 * @return 线程对象，如果未找到返回null
	 */
	public static Thread getThread(long threadId) {
		Thread[] threads;

		for (ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
			 threadGroup != null;
			 threadGroup = threadGroup.getParent()) {

			threads = new Thread[(int) (threadGroup.activeCount() * 1.2)];
			int count = threadGroup.enumerate(threads, true);

			for (int i = 0; i < count; ++i) {
				if (threadId == threads[i].getId()) {
					return threads[i];
				}
			}
		}

		return null;
	}
}
