package threadtutil.timer;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

import threadtutil.thread.ExecutorPool;
import threadtutil.timer.model.SerialTimeNode;
import threadtutil.timer.model.TimeNode;

/**
 * 定时器实现
 * 管理多个时间节点的执行，支持延迟执行、间隔执行和有限次执行
 * 支持普通节点和串行节点的执行
 */
public class Timer extends AbstractTimer<ExecutorPool> {

	public Timer() {
		super(new LinkedList<>());
	}

	/**
	 * 设置任务执行器
	 *
	 * @param runners 任务执行器实例
	 * @return 当前Timer实例
	 */
	@Override
	public Timer setRunners(ExecutorPool runners) {
		exit();
		this.runners = runners;
		(new Thread(this)).start();
		return this;
	}

	/**
	 * 执行时间节点
	 *
	 * @param timeNode 要执行的时间节点
	 * @return 任务的CompletableFuture
	 */
	@Override
	protected CompletableFuture<?> executeTimeNode(TimeNode<?> timeNode) {
		if (timeNode instanceof SerialTimeNode) {
			return runners.serialExecute((SerialTimeNode<?>) timeNode);
		} else {
			return runners.run(timeNode);
		}
	}

	/**
	 * 重新调度节点
	 *
	 * @param node 需要重新调度的节点
	 */
	@Override
	protected void rescheduleNode(TimeNode<?> node) {
		runners.run(() -> addNode(node));
	}
}