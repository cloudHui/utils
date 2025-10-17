package threadtutil.timer;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import threadtutil.timer.model.TimeNode;

/**
 * 无序定时器实现
 * 管理多个时间节点的执行，支持延迟执行、间隔执行和有限次执行
 */
public class DisorderTimer extends AbstractTimer<DisorderTimer.Runners<Runnable>> {

	public DisorderTimer() {
		super(new ArrayList<>());
	}

	/**
	 * 设置任务执行器
	 *
	 * @param runners 任务执行器实例
	 * @return 当前DisorderTimer实例
	 */
	@Override
	public DisorderTimer setRunners(Runners<Runnable> runners) {
		exit();
		this.runners = runners;
		this.runners.run(this);
		return this;
	}

	/**
	 * 注册普通时间节点（以秒为单位）
	 *
	 * @param <T>      参数类型
	 * @param delay    延迟时间（秒）
	 * @param interval 执行间隔（秒）
	 * @param count    执行次数（-1表示无限次）
	 * @param runner   任务执行器
	 * @param param    任务参数
	 */
	public <T> void register(int delay, int interval, int count, Runner<T> runner, T param) {
		register(delay * 1000L, interval * 1000L, count, runner, param);
	}

	/**
	 * 注册串行时间节点（以秒为单位）
	 *
	 * @param <T>      参数类型
	 * @param groupId  组ID
	 * @param delay    延迟时间（秒）
	 * @param interval 执行间隔（秒）
	 * @param count    执行次数（-1表示无限次）
	 * @param runner   任务执行器
	 * @param param    任务参数
	 */
	public <T> void registerSerial(int groupId, int delay, int interval, int count,
								   Runner<T> runner, T param) {
		registerSerial(groupId, delay * 1000L, interval * 1000L, count, runner, param);
	}

	/**
	 * 执行时间节点
	 *
	 * @param timeNode 要执行的时间节点
	 * @return 任务的CompletableFuture
	 */
	@Override
	protected CompletableFuture<?> executeTimeNode(TimeNode<?> timeNode) {
		return runners.run(timeNode);
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

	/**
	 * 任务执行器接口
	 *
	 * @param <T> 任务类型，必须是Runnable的子类
	 */
	@FunctionalInterface
	public interface Runners<T extends Runnable> {
		/**
		 * 执行任务
		 *
		 * @param task 要执行的任务
		 * @return 任务的CompletableFuture
		 */
		CompletableFuture<T> run(T task);
	}
}