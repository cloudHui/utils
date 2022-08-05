package threadtutil.timer.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import threadtutil.timer.Runner;

public class TimeNode<T> implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeNode.class);
	private final int id;
	private Runner runner;
	private T param;
	private int interval;
	private int count;
	private long triggerTime;

	public TimeNode(int id, Runner runner, T param, int delay, int interval) {
		this(id, runner, param, interval, delay, -1);
	}

	public TimeNode(int id, Runner runner, T param, int delay, int interval, int count) {
		this.id = id;
		this.runner = runner;
		this.param = param;
		this.interval = interval;
		this.count = count;
		this.triggerTime = System.currentTimeMillis() + (long)delay;
	}

	public int getId() {
		return this.id;
	}

	public long timeDifference(long time) {
		return this.triggerTime - time;
	}

	public boolean onTime(long time) {
		return time >= this.triggerTime;
	}

	public void refreshTriggerTime() {
		this.triggerTime += (long)this.interval;
	}

	public boolean finished() {
		return 0 == this.count;
	}

	public void run() {
		if (this.count > 0) {
			--this.count;
		}

		try {
			if (this.runner.run(this.param)) {
				this.count = 0;
			}
		} catch (Exception var2) {
			LOGGER.error("failed for on time with {}", this.runner.toString(), var2);
		}

	}
}
