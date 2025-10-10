package net.connect.handle;

import java.util.concurrent.CompletableFuture;

import net.message.TCPMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompleterTcpMsg extends CompletableFuture<TCPMessage> implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(CompleterTcpMsg.class);
	private final long timeout;
	public Throwable ex;
	public TCPMessage msg;

	public CompleterTcpMsg(long timeout) {
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