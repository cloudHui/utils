package threadtutil.timer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import threadtutil.lock.TimeSignal;
import threadtutil.timer.model.SerialTimeNode;
import threadtutil.timer.model.TimeNode;

public class DisorderTimer implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(DisorderTimer.class);
	private final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
	private final List<TimeNode<?>> nodes = new ArrayList<>();
	private final Lock lock = new ReentrantLock(false);
	private final TimeSignal timeSignal = new TimeSignal();
	private Runners<Runnable> runners;
	private int loops = 0;

	public DisorderTimer() {
	}

	public DisorderTimer setRunners(Runners<Runnable> runners) {
		this.exit();
		this.runners = runners;
		this.runners.run(this);
		return this;
	}

	public <T> void register(int delay, int interval, int count, Runner<T> runner, T param) {
		this.addNode(new TimeNode(this.ID_GENERATOR.incrementAndGet(), runner, param, delay * 1000L, interval * 1000L, count));
	}

	public <T> void registerSerial(int groupId, int delay, int interval, int count, Runner<T> runner, T param) {
		this.addNode(new SerialTimeNode(groupId, this.ID_GENERATOR.incrementAndGet(), runner, param, delay * 1000L, interval * 1000L, count));
	}

	private void addNode(TimeNode<?> timeNode) {
		this.lock.lock();

		try {
			this.nodes.add(timeNode);
		} finally {
			this.lock.unlock();
		}

		this.timeSignal.notifySignal();
	}

	public void exit() {
		++this.loops;
	}

	@Override
	public void run() {
		long waitTime;
		for (int loop = this.loops; loop == this.loops; this.timeSignal.waitSignal(waitTime)) {
			waitTime = 9223372036854775807L;
			this.lock.lock();

			try {
				long now = System.currentTimeMillis();
				Iterator<TimeNode<?>> it = this.nodes.iterator();

				while (it.hasNext()) {
					TimeNode<?> timeNode = it.next();
					if (null == timeNode) {
						it.remove();
					} else {
						long diff = timeNode.timeDifference(now);
						if (diff > 0L) {
							waitTime = Math.min(diff, waitTime);
						} else {
							it.remove();
							CompletableFuture<? extends Runnable> future = this.runners.run(timeNode);
							if (null != future) {
								future.whenComplete((n, t) -> {
									if (n instanceof TimeNode) {
										TimeNode<?> node = (TimeNode<?>) n;
										if (node.unFinished()) {
											node.refreshTriggerTime();
											this.runners.run(() -> this.addNode(node));
										}
									}

								});
							}
						}
					}
				}
			} catch (Exception var12) {
				LOGGER.error("Timer", var12);
			} finally {
				this.lock.unlock();
			}
		}
	}

	@FunctionalInterface
	public interface Runners<T extends Runnable> {
		CompletableFuture<T> run(T var1);
	}
}
