package threadtutil.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeSignal {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeSignal.class);
	private volatile boolean notified = false;

	public TimeSignal() {
	}

	public void notifySignal() {
		synchronized (this) {
			try {
				super.notify();
			} catch (Exception var8) {
				LOGGER.error("", var8);
			} finally {
				this.notified = true;
			}

		}
	}

	public void waitSignal(long waitTime) {
		if (waitTime > 0L) {
			synchronized (this) {
				if (!this.notified) {
					try {
						super.wait(waitTime);
					} catch (Exception var6) {
						LOGGER.error("", var6);
					}
				}
			}
		}

		this.notified = false;
	}
}
