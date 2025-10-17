package threadtutil.thread;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import threadtutil.thread.model.TaskList;
import threadtutil.utils.TimeUtils;

/**
 * 执行器池类，提供任务执行和串行化执行功能
 * 支持将具有相同groupId的任务串行化执行
 */
public class ExecutorPool {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorPool.class);

	// 线程池实例
	private final ThreadPool threadPool;
	// 任务列表数组，用于串行化执行
	private final TaskList[] taskLists;

	// 超时时间常量
	private static final long PROCESS_TIMEOUT_MS = 5000L;
	private static final long STACK_TRACE_TIMEOUT_MS = 60000L;

	/**
	 * 构造函数，使用默认大小创建执行器池
	 *
	 * @param executorName 执行器名称
	 */
	public ExecutorPool(String executorName) {
		this(executorName, 0);
	}

	/**
	 * 构造函数，指定大小创建执行器池
	 *
	 * @param executorName 执行器名称
	 * @param size         线程池大小
	 */
	public ExecutorPool(String executorName, int size) {
		this.threadPool = new ThreadPool(executorName,
				(size < 1) ? TimeUtils.PROCESS_NUMBER : size);
		this.taskLists = new TaskList[this.threadPool.size()];

		for (int i = 0; i < this.threadPool.size(); ++i) {
			this.taskLists[i] = new TaskList();
		}

		LOGGER.info("[init] thread pool size:{}", this.threadPool.size());
	}

	/**
	 * 获取线程池大小
	 *
	 * @return 线程池大小
	 */
	public int size() {
		return this.threadPool.size();
	}

	/**
	 * 执行普通任务
	 *
	 * @param runnable 可执行任务
	 */
	public void execute(Runnable runnable) {
		this.threadPool.execute(runnable);
	}

	/**
	 * 串行化执行任务，具有相同groupId的任务将按顺序执行
	 *
	 * @param task 任务对象
	 * @return CompletableFuture对象，可用于获取执行结果
	 */
	public CompletableFuture<Task> serialExecute(Task task) {
		TaskNode taskNode = new TaskNode(task);
		// 根据groupId计算任务应该分配到的任务列表索引
		int index = Math.abs(taskNode.groupId() % this.size());
		this.taskLists[index].add(taskNode);

		this.threadPool.execute(() -> processTasks(index));
		return taskNode.completableFuture;
	}

	/**
	 * 处理任务列表中的任务
	 *
	 * @param startIndex 起始索引
	 */
	private void processTasks(int startIndex) {
		try {
			long threadId = Thread.currentThread().getId();
			int i = 0;
			int idx = startIndex;

			for (int size = this.size(); i < size; ++i) {
				// 循环遍历任务列表
				if (idx >= size) {
					idx = 0;
				}

				if (this.taskLists[idx].isNotEmpty()) {
					// 检查任务列表是否正在被其他线程处理
					if (this.taskLists[idx].isBusy() && !this.taskLists[idx].isSelf(threadId)) {
						checkAndLogTimeout(idx);
					}
					// 获取处理权限并执行任务
					else if (this.taskLists[idx].getProcessingAuthority(threadId)) {
						processTaskList(idx, threadId);
						this.taskLists[idx].releaseProcessingAuthority(threadId);
					}
				}

				++idx;
			}
		} catch (Exception e) {
			LOGGER.error("Error processing tasks", e);
		}
	}

	/**
	 * 检查并记录超时情况
	 *
	 * @param idx 任务列表索引
	 */
	private void checkAndLogTimeout(int idx) {
		Date time = this.taskLists[idx].getTime();
		if (time != null && (TimeUtils.time() - time.getTime() > PROCESS_TIMEOUT_MS)) {
			long processorId = this.taskLists[idx].getProcessorId();
			String processorName = this.threadPool.getThreadName(processorId);

			LOGGER.error("[THREAD POOL] process is too long(idx:{}, processor:{}:{}, time:{})",
					idx, processorId, processorName, time);

			// 如果超时时间过长，输出线程堆栈信息
			if (TimeUtils.time() - time.getTime() > STACK_TRACE_TIMEOUT_MS) {
				Thread thread = ThreadPool.getThread(processorId);
				if (thread != null) {
					StackTraceElement[] elements = thread.getStackTrace();
					StringBuilder sb = new StringBuilder();
					sb.append("Stack for ").append(thread.getId())
							.append(":").append(thread.getName()).append(":");

					for (StackTraceElement element : elements) {
						sb.append("\n ").append(element.getClassName())
								.append(".").append(element.getMethodName())
								.append("(").append(element.getFileName())
								.append(":").append(element.getLineNumber()).append(")");
					}

					LOGGER.error(sb.toString());
				}
			}
		}
	}

	/**
	 * 处理指定任务列表中的所有任务
	 *
	 * @param idx      任务列表索引
	 * @param threadId 当前线程ID
	 */
	private void processTaskList(int idx, long threadId) {
		TaskNode task;
		while ((task = (TaskNode) this.taskLists[idx].pop()) != null) {
			this.taskLists[idx].updateTime();

			try {
				task.run();
				task.completableFuture.complete(task.t);
			} catch (Throwable e) {
				LOGGER.error("RUN1:{}", task.toString(), e);

				try {
					task.completableFuture.completeExceptionally(e);
				} catch (Throwable e2) {
					LOGGER.error("RUN2:{}", task.toString(), e2);
				}
			}
		}
	}

	/**
	 * 执行Runnable任务并返回CompletableFuture
	 *
	 * @param runnable 可执行任务
	 * @return CompletableFuture对象
	 */
	public CompletableFuture<Void> run(Runnable runnable) {
		CompletableFuture<Void> future = new CompletableFuture<>();
		this.threadPool.execute(() -> {
			try {
				runnable.run();
				future.complete(null);
			} catch (Throwable throwable) {
				future.completeExceptionally(throwable);
			}
		});
		return future;
	}

	/**
	 * 获取底层ExecutorService
	 *
	 * @return ExecutorService实例
	 */
	public ExecutorService getExecutorService() {
		return this.threadPool.getPools();
	}

	/**
	 * 任务节点类，包装Task对象并提供CompletableFuture支持
	 */
	private static class TaskNode implements Task {
		// 原始任务对象
		public final Task t;
		// 用于异步处理结果的CompletableFuture
		public final CompletableFuture<Task> completableFuture;

		/**
		 * 构造函数
		 *
		 * @param task 原始任务对象
		 */
		public TaskNode(Task task) {
			this.t = task;
			this.completableFuture = new CompletableFuture<>();
		}

		@Override
		public int groupId() {
			return this.t.groupId();
		}

		@Override
		public void run() {
			this.t.run();
		}

		@Override
		public String toString() {
			return String.format("TaskNode %s", this.t.toString());
		}
	}
}
