package net.connect.handle;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompleterGroup implements Runnable, Comparable<CompleterGroup> {
	private static final Logger LOGGER = LoggerFactory.getLogger(CompleterGroup.class);
	private static final Throwable TIMEOUT = new RuntimeException("timeout");
	private static final Throwable UNKNOWN_EXCEPTION = new RuntimeException("Unknown exception occurred!");

	private final Map<Integer, Completer> completerMap = new ConcurrentHashMap<>(128);
	private final Map<Integer, CompleterTcpMsg> completerTcpMsgMap = new ConcurrentHashMap<>(128);
	private EventLoop executors; // 移除了final修饰符以允许在destroy中置null

	private static final AtomicInteger SEQUENCE_GENERATOR = new AtomicInteger(0);
	private static final Set<CompleterGroup> RUNNER_GROUPS = new ConcurrentSkipListSet<>();
	private static final Thread CHECKER_THREAD = createCheckerThread();

	private volatile boolean destroyed = false;

	@Override
	public int compareTo(CompleterGroup other) {
		return Integer.compare(this.hashCode(), other.hashCode());
	}

	public CompleterGroup(EventLoop eventExecutors) {
		this.executors = eventExecutors;
		RUNNER_GROUPS.add(this);
	}

	public int getSequence() {
		return SEQUENCE_GENERATOR.updateAndGet(seq -> seq >= Integer.MAX_VALUE ? 1 : seq + 1);
	}

	public void addCompleter(Integer sequence, Completer completer) {
		if (!destroyed) {
			completerMap.put(sequence, completer);
		}
	}

	public Completer popCompleter(int sequence) {
		return completerMap.remove(sequence);
	}

	public void addCompleterTcpMsg(int sequence, CompleterTcpMsg completer) {
		if (!destroyed) {
			completerTcpMsgMap.put(sequence, completer);
		}
	}

	public CompleterTcpMsg popCompleterTcpMsg(Integer sequence) {
		return completerTcpMsgMap.remove(sequence);
	}

	public Set<Integer> getSequences() {
		return new HashSet<>(completerMap.keySet());
	}

	public Set<Integer> getTcpSequences() {
		return new HashSet<>(completerTcpMsgMap.keySet());
	}

	public void destroy() {
		destroyed = true;
		RUNNER_GROUPS.remove(this);

		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			LOGGER.debug("Interrupted during destroy wait period", e);
		}

		// 处理 Completer
		processRemainingCompletes(completerMap);
		// 处理 CompleterTcpMsg
		processRemainingCompletes(completerTcpMsgMap);

		executors = null;
	}

	@Override
	public void run() {
		if (destroyed || executors == null) {
			return;
		}

		try {
			long currentTime = System.currentTimeMillis();

			// 处理超时的 Completer
			processTimeoutItems(completerMap, currentTime);
			// 处理超时的 CompleterTcpMsg
			processTimeoutItems(completerTcpMsgMap, currentTime);

		} catch (Exception e) {
			LOGGER.debug("Exception during timeout check", e);
		}
	}

	private <T extends CompleterBase> void processRemainingCompletes(Map<Integer, T> map) {
		if (map.isEmpty()) {
			return;
		}

		Set<Integer> keys = new HashSet<>(map.keySet());
		for (Integer id : keys) {
			T completer = map.remove(id);
			if (completer != null && executors != null) {
				completer.setEx(CompleterGroup.UNKNOWN_EXCEPTION);
				executors.execute(completer);
			}
		}
	}

	private <T extends CompleterBase> void processTimeoutItems(Map<Integer, T> map, long currentTime) {

		Set<Integer> timeoutKeys = new HashSet<>();
		map.forEach((key, completer) -> {
			if (completer.isTimeout(currentTime)) {
				timeoutKeys.add(key);
			}
		});

		if (!timeoutKeys.isEmpty()) {
			for (Integer id : timeoutKeys) {
				T completer = map.remove(id);
				if (completer != null) {
					completer.setEx(CompleterGroup.TIMEOUT);
					executors.execute(completer);
				}
			}
		}
	}

	private static Thread createCheckerThread() {
		Thread thread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					long startTime = System.currentTimeMillis();

					// 执行所有runner groups
					for (CompleterGroup group : RUNNER_GROUPS) {
						try {
							group.run();
						} catch (Exception e) {
							LOGGER.error("Error executing completer group", e);
						}
					}

					long elapsed = System.currentTimeMillis() - startTime;
					long sleepTime = Math.max(10L, 1000L - elapsed);

					TimeUnit.MILLISECONDS.sleep(sleepTime);

				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				} catch (Exception e) {
					LOGGER.error("Unexpected error in checker thread", e);
					try {
						TimeUnit.MILLISECONDS.sleep(1000);
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
						break;
					}
				}
			}
			LOGGER.info("Checker thread stopped");
		});

		thread.setDaemon(true);
		thread.setName("CompleterGroup-Checker");
		return thread;
	}

	public static void shutdown() {
		if (CHECKER_THREAD.isAlive()) {
			CHECKER_THREAD.interrupt();
		}
	}

	static {
		CHECKER_THREAD.start();
		Runtime.getRuntime().addShutdownHook(new Thread(CompleterGroup::shutdown));
	}
}