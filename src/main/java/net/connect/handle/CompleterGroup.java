package net.connect.handle;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompleterGroup implements Runnable, Comparable<CompleterGroup> {
	private static final Logger LOGGER = LoggerFactory.getLogger(Completer.class);
	private static final Throwable TIMEOUT = new RuntimeException("timeout");
	private final Map<Long, Completer> completerMap = new ConcurrentHashMap<>(128);
	private final Map<Long, CompleterTcpMsg> completerTcpMsgMap = new ConcurrentHashMap<>(128);
	private EventLoop executors;
	private static int sequenceId = 0;
	private static final Set<Runnable> runners = new ConcurrentSkipListSet<>((o1, o2) -> 0);
	private static final Thread checker = new Thread(() -> {
		long waitTime;

		while (true) {
			while (true) {
				waitTime = System.currentTimeMillis();
				boolean check = false;

				try {
					check = true;
					runners.forEach(Runnable::run);
					check = false;
					break;
				} catch (Exception exception) {
					LOGGER.error("", exception);
					check = false;
				} finally {
					if (check) {
						waitTime = 1000L - (System.currentTimeMillis() - waitTime);
						if (waitTime > 10L) {
							synchronized (runners) {
								try {
									runners.wait(waitTime);
								} catch (Exception exception) {
									LOGGER.error("", exception);
								}
							}
						}

					}
				}

				waitTime = 1000L - (System.currentTimeMillis() - waitTime);
				if (waitTime > 10L) {
					synchronized (runners) {
						try {
							runners.wait(waitTime);
						} catch (Exception exception) {
							LOGGER.error("", exception);
						}
					}
				}
			}

			waitTime = 1000L - (System.currentTimeMillis() - waitTime);
			if (waitTime > 10L) {
				synchronized (runners) {
					try {
						runners.wait(waitTime);
					} catch (Exception exception) {
						LOGGER.error("", exception);
					}
				}
			}
		}
	});

	@Override
	public int compareTo(CompleterGroup o) {
		return this.equals(o) ? 0 : 1;
	}

	public CompleterGroup(EventLoop eventExecutors) {
		executors = eventExecutors;
		runners.add(this);
	}

	public synchronized int getSequence() {
		if (sequenceId >= Integer.MAX_VALUE) {
			sequenceId = 1;
		}
		return ++sequenceId;
	}

	public void addCompleter(long sequence, Completer completer) {
		completerMap.put(sequence, completer);
	}

	public Completer popCompleter(long sequence) {
		return completerMap.remove(sequence);
	}

	public void addCompleterTcpMsg(long sequence, CompleterTcpMsg completer) {
		completerTcpMsgMap.put(sequence, completer);
	}

	public CompleterTcpMsg popCompleterTcpMsg(long sequence) {
		return completerTcpMsgMap.remove(sequence);
	}

	public Set<Long> getSequences() {
		return completerMap.keySet();
	}

	public Set<Long> getTcpSequences() {
		return completerTcpMsgMap.keySet();
	}

	public void destroy() {
		runners.remove(this);

		try {
			Thread.sleep(3000L);
		} catch (Exception ignored) {
		}

		Completer completer;

		while (!completerMap.isEmpty()) {
			Throwable ex = new RuntimeException("Unknown exception occurred！");
			Set<Long> keys = completerMap.keySet();

			for (Long id : keys) {
				completer = completerMap.remove(id);
				if (null != completer) {
					completer.ex = ex;
					executors.execute(completer);
				}
			}
		}
		CompleterTcpMsg completerTcpMsg;
		while (!completerTcpMsgMap.isEmpty()) {
			Throwable ex = new RuntimeException("Unknown exception occurred！");
			Set<Long> keys = completerTcpMsgMap.keySet();

			for (Long id : keys) {
				completerTcpMsg = completerTcpMsgMap.remove(id);
				if (null != completerTcpMsg) {
					completerTcpMsg.ex = ex;
					executors.execute(completerTcpMsg);
				}
			}
		}
		executors = null;
	}

	@Override
	public void run() {
		try {
			Set<Long> seq = new HashSet<>();
			long nowTime = System.currentTimeMillis();
			completerMap.forEach((k, o) -> {
				if (o.isTimeout(nowTime)) {
					seq.add(k);
				}

			});
			if (!seq.isEmpty()) {
				Completer completer;

				for (Long id : seq) {
					completer = completerMap.remove(id);
					if (null != completer) {
						completer.ex = TIMEOUT;
						executors.execute(completer);
					}
				}
			}

			completerTcpMsgMap.forEach((k, o) -> {
				if (o.isTimeout(nowTime)) {
					seq.add(k);
				}

			});
			if (!seq.isEmpty()) {
				CompleterTcpMsg completer;

				for (Long id : seq) {
					completer = completerTcpMsgMap.remove(id);
					if (null != completer) {
						completer.ex = TIMEOUT;
						executors.execute(completer);
					}
				}
			}
		} catch (Exception ignored) {
		}

	}

	static {
		checker.start();
	}
}