package threadtutil.timer.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import threadtutil.timer.Runner;

public class TimeNode<T> implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeNode.class);
    private final int id;
    private final Runner<T> runner;
    private final T param;
    private final long interval;
    private int count;
    private long triggerTime;

    public TimeNode(int id, Runner<T> runner, T param, long delay, long interval) {
        this(id, runner, param, delay, interval, -1);
    }

    public TimeNode(int id, Runner<T> runner, T param, long delay, long interval, int count) {
        this.id = id;
        this.runner = runner;
        this.param = param;
        this.interval = interval;
        this.count = count;
        this.triggerTime = System.currentTimeMillis() + delay;
        LOGGER.debug("TimeNode created with id: {}, delay: {}ms, interval: {}ms, count: {}", id, delay, interval, count);
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
        this.triggerTime += this.interval;
        LOGGER.debug("Refreshed trigger time for node id: {}, new trigger time: {}", this.id, this.triggerTime);
    }

    public boolean unFinished() {
        boolean unfinished = 0 != this.count;
        LOGGER.trace("Checking if node id: {} is unfinished: {}", this.id, unfinished);
        return unfinished;
    }

    @Override
    public void run() {
        if (this.count > 0) {
            --this.count;
            LOGGER.debug("Decrementing count for node id: {}, remaining executions: {}", this.id, this.count);
        }

        try {
            LOGGER.debug("Executing time node with id: {}", this.id);
            if (this.runner.run(this.param)) {
                this.count = 0;
                LOGGER.debug("Runner requested termination for node id: {}", this.id);
            }
        } catch (Exception var2) {
            LOGGER.error("Failed to execute time node with id: {} using runner: {}", this.id, this.runner.toString(), var2);
        }
    }
}
