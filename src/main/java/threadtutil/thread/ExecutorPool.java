package threadtutil.thread;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import threadtutil.thread.model.TaskList;
import threadtutil.utils.TimeUtils;

public class ExecutorPool {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorPool.class);
	private final ThreadPool threadPool;
	private final TaskList[] taskLists;

	public ExecutorPool(String executorName) {
		this(executorName, 0);
	}

	public ExecutorPool(String executorName, int size) {
		this.threadPool = new ThreadPool(executorName, size < 1 ? TimeUtils.PROCESS_NUMBER : size);
		this.taskLists = new TaskList[this.threadPool.size()];

		for (int i = 0; i < this.threadPool.size(); ++i) {
			this.taskLists[i] = new TaskList();
		}

		LOGGER.info("[init] thread pool size:{}", this.threadPool.size());
	}

	public int size() {
		return this.threadPool.size();
	}

	public void execute(Runnable r) {
		this.threadPool.execute(r);
	}

	public CompletableFuture<Task> serialExecute(Task t) {
		TaskNode taskNode = new TaskNode(t);
		int index = Math.abs(taskNode.groupId() % this.size());
		this.taskLists[index].add(taskNode);
		this.threadPool.execute(() -> {
			try {
				long threadId = Thread.currentThread().getId();
				TaskNode task;
				int i = 0;
				int idx = index;

				for (int size = this.size(); size > i; ++idx) {
					if (idx >= size) {
						idx = 0;
					}

					if (this.taskLists[idx].isNotEmpty()) {
						if (this.taskLists[idx].isBusy() && !this.taskLists[idx].isSelf(threadId)) {
							Date time = this.taskLists[idx].getTime();
							if (null != time && TimeUtils.time() - time.getTime() > 5000L) {
								LOGGER.error("[THREAD POOL] process is to long(idx:{}, processor:{}:{}, time:{})",
										idx, this.taskLists[idx].getProcessorId(),
										this.threadPool.getThreadName(this.taskLists[idx].getProcessorId()), time);
								if (TimeUtils.time() - time.getTime() > 60000L) {
									Thread thread = ThreadPool.getThread(this.taskLists[idx].getProcessorId());
									if (null != thread) {
										StackTraceElement[] element = thread.getStackTrace();
										StringBuilder sb = new StringBuilder();
										sb.append("Stack for ").append(thread.getId()).append(":")
												.append(thread.getName()).append(":");
										int j = 0;

										for (int jSize = element.length; j < jSize; ++j) {
											sb.append("\n ").append(element[j].getClassName())
													.append(".").append(element[j].getMethodName())
													.append("(").append(element[j].getFileName())
													.append(":").append(element[j].getLineNumber()).append(")");
										}

										LOGGER.error(sb.toString());
									}
								}
							}
						} else if (this.taskLists[idx].getProcessingAuthority(threadId)) {
							while ((task = (TaskNode) this.taskLists[idx].pop()) != null) {
								this.taskLists[idx].updateTime();

								try {
									task.run();
									task.completableFuture.complete(task.t);
								} catch (Throwable var15) {
									LOGGER.error("RUN1:{}", task.toString(), var15);

									try {
										task.completableFuture.completeExceptionally(var15);
									} catch (Throwable var14) {
										LOGGER.error("RUN2:{}", task.toString(), var14);
									}
								}
							}

							this.taskLists[idx].releaseProcessingAuthority(threadId);
						}
					}

					++i;
				}
			} catch (Exception var16) {
				LOGGER.error("", var16);
			}

		});
		return taskNode.completableFuture;
	}

	public CompletableFuture run(Runnable r) {
		CompletableFuture future = new CompletableFuture();
		this.threadPool.execute(() -> {
			try {
				r.run();
				future.complete(r);
			} catch (Throwable var3) {
				future.completeExceptionally(var3);
			}

		});
		return future;
	}

	public ExecutorService getExecutorService() {
		return this.threadPool.getPools();
	}

	private static class TaskNode implements Task {
		public final Task t;
		public final CompletableFuture<Task> completableFuture;

		public TaskNode(Task t) {
			this.t = t;
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
