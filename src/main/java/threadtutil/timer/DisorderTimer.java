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
    private final List<TimeNode> nodes = new ArrayList();
    private final Lock lock = new ReentrantLock(false);
    private final TimeSignal timeSignal = new TimeSignal();
    private Runners<Runnable> runners;
    private int loops = 0;
    private long waitTime = 9223372036854775807L;

    public DisorderTimer() {
    }

    public DisorderTimer setRunners(Runners runners) {
        this.exit();
        this.runners = runners;
        this.runners.run(this);
        return this;
    }

    public <T> void register(int delay, int interval, int count, Runner<T> runner, T param) {
        this.addNode(new TimeNode(this.ID_GENERATOR.incrementAndGet(), runner, param, delay * 1000, interval * 1000, count));
    }

    public <T> void registerSerial(int groupId, int delay, int interval, int count, Runner<T> runner, T param) {
        this.addNode(new SerialTimeNode(groupId, this.ID_GENERATOR.incrementAndGet(), runner, param, delay * 1000, interval * 1000, count));
    }

    private void addNode(TimeNode timeNode) {
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

    public void run() {
        for(int loop = this.loops; loop == this.loops; this.timeSignal.waitSignal(this.waitTime)) {
            this.waitTime = 9223372036854775807L;
            this.lock.lock();

            try {
                long now = System.currentTimeMillis();
                Iterator it = this.nodes.iterator();

                while(it.hasNext()) {
                    TimeNode timeNode = (TimeNode)it.next();
                    if (null == timeNode) {
                        it.remove();
                    } else {
                        long diff = timeNode.timeDifference(now);
                        if (diff > 0L) {
                            this.waitTime = Math.min(diff, this.waitTime);
                        } else {
                            it.remove();
                            CompletableFuture future = this.runners.run(timeNode);
                            if (null != future) {
                                future.whenComplete((n, t) -> {
                                    if (n instanceof TimeNode) {
                                        TimeNode node = (TimeNode)n;
                                        if (!node.finished()) {
                                            node.refreshTriggerTime();
                                            this.runners.run(() -> {
                                                this.addNode(node);
                                            });
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
}
