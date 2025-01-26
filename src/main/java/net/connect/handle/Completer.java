package net.connect.handle;

import java.util.concurrent.CompletableFuture;

import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Completer extends CompletableFuture<Message> implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(Completer.class);
	private final long timeout;
	public Throwable ex;
	public Message msg;

	public Completer(long timeout) {
		this.timeout = timeout * 1000L + System.currentTimeMillis();
	}

	public boolean isTimeout(long time) {
		return time >= timeout;
	}

	@Override
	public void run() {
		try {
			if (null != ex) {
				completeExceptionally(ex);
			} else {
				complete(msg);
			}
		} catch (Exception exception) {
			LOGGER.error("", exception);
		}

	}
}