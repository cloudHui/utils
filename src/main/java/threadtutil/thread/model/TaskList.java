package threadtutil.thread.model;

import threadtutil.thread.Task;
import threadtutil.utils.TimeUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class TaskList {
	private final AtomicLong processor;
	private Date time;
	private final List<Task> tasks;


	public TaskList() {
		tasks = new CopyOnWriteArrayList<>();
		this.processor = new AtomicLong(0L);
	}

	public boolean isBusy() {
		return this.processor.get() != 0L;
	}

	public boolean isSelf(long processorId) {
		return this.getProcessorId() == processorId;
	}

	public boolean getProcessingAuthority(long processorId) {
		if (this.processor.compareAndSet(0L, processorId)) {
			this.time = TimeUtils.now();
			return true;
		} else {
			return this.processor.get() == processorId;
		}
	}

	public void releaseProcessingAuthority(long processorId) {
		if (this.processor.compareAndSet(processorId, 0L)) {
			this.time = null;
		}
	}

	public long getProcessorId() {
		return this.processor.get();
	}

	public void updateTime() {
		this.time = TimeUtils.now();
	}

	public Date getTime() {
		return this.time;
	}

	public void add(Task task) {
		tasks.add(task);
	}

	public Task pop() {
		if (isNotEmpty()) {

			Task var1;
			this.time = TimeUtils.now();
			var1 = tasks.remove(0);
			return var1;

		}
		return null;
	}

	public boolean isNotEmpty() {
		return !tasks.isEmpty();
	}

}
