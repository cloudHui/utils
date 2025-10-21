package net.connect.handle;

import java.util.concurrent.CompletableFuture;

import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Completer extends CompletableFuture<Message> implements CompleterBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(Completer.class);
	private final long timeout;
	private Throwable ex;
	private Message msg;

	public Completer(long timeout) {
		this.timeout = timeout * 1000L + System.currentTimeMillis();
	}

	public boolean isTimeout(long time) {
		return time >= timeout;
	}

	@Override
	public void setEx(Throwable ex) {
		this.ex = ex;
	}

	public void setMsg(Message msg) {
		this.msg = msg;
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
			LOGGER.error("Error completing future", exception);
		}
	}
}