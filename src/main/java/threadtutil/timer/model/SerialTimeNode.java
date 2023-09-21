package threadtutil.timer.model;

import threadtutil.thread.Task;
import threadtutil.timer.Runner;

public class SerialTimeNode<T> extends TimeNode<T> implements Task {
	private final int groupId;

	public SerialTimeNode(int groupId, int id, Runner runner, T param, long delay, long interval) {
		this(groupId, id, runner, param, interval, delay, -1);
	}

	public SerialTimeNode(int groupId, int id, Runner runner, T param, long delay, long interval, int count) {
		super(id, runner, param, delay, interval, count);
		this.groupId = groupId;
	}

	@Override
	public int groupId() {
		return this.groupId;
	}
}
