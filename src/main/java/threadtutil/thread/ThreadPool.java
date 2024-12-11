package threadtutil.thread;

import threadtutil.utils.TimeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool {
	private final ExecutorService pools;
	private final ThreadGroup group;
	private final String threadPrefix;
	private final AtomicInteger threadNumber;
	private final Map<Long, String> threadName;
	private final int size;

	public ThreadPool(String prefix) {
		this(prefix, 0);
	}

	public ThreadPool(String prefix, int size) {
		this.threadName = new HashMap<>(size);
		SecurityManager s = System.getSecurityManager();
		this.group = null != s ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		this.threadPrefix = prefix;
		this.threadNumber = new AtomicInteger(0);
		this.size = size > 0 ? size : TimeUtils.PROCESS_NUMBER;
		this.pools = new ThreadPoolExecutor(this.size, this.size, 0, TimeUnit.SECONDS,
				new LinkedBlockingDeque<>(1000), (r) -> {
			Thread t = new Thread(this.group, r, this.threadPrefix + "_" +
					this.threadNumber.incrementAndGet(), 0L);
			if (t.isDaemon()) {
				t.setDaemon(false);
			}

			if (t.getPriority() != 5) {
				t.setPriority(5);
			}

			this.threadName.put(t.getId(), t.getName());
			return t;
		});
	}

	public final int size() {
		return this.size;
	}

	public ExecutorService getPools() {
		return this.pools;
	}

	public Future<?> execute(Runnable r) {
		return this.pools.submit(r);
	}

	public void close() {
		this.pools.shutdown();
	}

	protected String getThreadName(long threadId) {
		return this.threadName.get(threadId);
	}

	public static Thread getThread(long threadId) {
		Thread[] threads;

		for (ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
		     null != threadGroup; threadGroup = threadGroup.getParent()) {
			threads = new Thread[(int) ((double) threadGroup.activeCount() * 1.2D)];
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
