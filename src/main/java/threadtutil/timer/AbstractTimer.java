package threadtutil.timer;

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

/**
 * 抽象定时器基类
 * 提供通用的定时器功能，支持延迟执行、间隔执行和有限次执行
 */
public abstract class AbstractTimer<T_RUNNERS> implements Runnable {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTimer.class);
    
    // ID生成器，用于为每个时间节点生成唯一ID
    protected final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    
    // 时间节点列表
    protected final List<TimeNode<?>> nodes;
    
    // 用于保护nodes列表的锁
    protected final Lock lock = new ReentrantLock(false);
    
    // 时间信号，用于线程等待和唤醒
    protected final TimeSignal timeSignal = new TimeSignal();
    
    // 任务执行器
    protected T_RUNNERS runners;
    
    // 循环计数器，用于优雅退出
    protected int loops = 0;


    protected static final long WAIT_TIME = 180000L;

    /**
     * 构造函数
     * @param nodes 时间节点列表的具体实现
     */
    protected AbstractTimer(List<TimeNode<?>> nodes) {
        this.nodes = nodes;
    }

    /**
     * 设置任务执行器
     * @param runners 任务执行器实例
     * @return 当前定时器实例
     */
    public abstract AbstractTimer<T_RUNNERS> setRunners(T_RUNNERS runners);

    /**
     * 注册普通时间节点
     * @param <T> 参数类型
     * @param delay 延迟时间（毫秒）
     * @param interval 执行间隔（毫秒）
     * @param count 执行次数（-1表示无限次）
     * @param runner 任务执行器
     * @param param 任务参数
     */
    public <T> void register(long delay, long interval, int count, Runner<T> runner, T param) {
        addNode(new TimeNode<>(ID_GENERATOR.incrementAndGet(), runner, param, delay, interval, count));
    }

    /**
     * 注册串行时间节点（同一组内的节点会串行执行）
     * @param <T> 参数类型
     * @param groupId 组ID
     * @param delay 延迟时间（毫秒）
     * @param interval 执行间隔（毫秒）
     * @param count 执行次数（-1表示无限次）
     * @param runner 任务执行器
     * @param param 任务参数
     */
    public <T> void registerSerial(int groupId, long delay, long interval, int count, 
                                   Runner<T> runner, T param) {
        addNode(new SerialTimeNode<>(groupId, ID_GENERATOR.incrementAndGet(), 
                                   runner, param, delay, interval, count));
    }

    /**
     * 添加时间节点到列表
     * @param timeNode 要添加的时间节点
     */
    protected void addNode(TimeNode<?> timeNode) {
        lock.lock();
        try {
            nodes.add(timeNode);
        } finally {
            lock.unlock();
        }
        // 添加新节点后唤醒等待线程
        timeSignal.notifySignal();
    }

    /**
     * 退出定时器，停止当前循环
     */
    public void exit() {
        ++loops;
    }

    /**
     * 执行时间节点
     * @param timeNode 要执行的时间节点
     * @return 任务的CompletableFuture
     */
    protected abstract CompletableFuture<?> executeTimeNode(TimeNode<?> timeNode);

    /**
     * 重新调度节点
     * @param node 需要重新调度的节点
     */
    protected abstract void rescheduleNode(TimeNode<?> node);

    /**
     * 主运行循环
     * 检查时间节点是否到达执行时间，执行到达时间的节点
     */
    @Override
    public void run() {
        long waitTime;
        // 循环直到loops发生变化（表示需要退出）
        for (int loop = loops; loop == loops; timeSignal.waitSignal(waitTime)) {
            // 获取初始等待时间
            waitTime = WAIT_TIME;
            lock.lock();

            try {
                long now = System.currentTimeMillis();
                Iterator<TimeNode<?>> it = nodes.iterator();

                while (it.hasNext()) {
                    TimeNode<?> timeNode = it.next();
                    if (null == timeNode) {
                        // 移除空节点
                        it.remove();
                    } else {
                        long diff = timeNode.timeDifference(now);
                        if (diff > 0L) {
                            // 节点还未到达执行时间，更新最小等待时间
                            waitTime = Math.min(diff, waitTime);
                        } else {
                            // 节点到达执行时间，从列表中移除并执行
                            it.remove();
                            CompletableFuture<?> future = executeTimeNode(timeNode);
                            if (null != future) {
                                future.whenComplete((result, throwable) -> {
                                    if (result instanceof TimeNode) {
                                        TimeNode<?> node = (TimeNode<?>) result;
                                        if (node.unFinished()) {
                                            // 节点未执行完成，刷新触发时间并重新添加到列表
                                            node.refreshTriggerTime();
                                            rescheduleNode(node);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Timer execution error", e);
            } finally {
                lock.unlock();
            }
        }
    }
}