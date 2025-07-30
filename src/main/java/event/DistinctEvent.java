package event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DistinctEvent {

	protected final static Logger logger = LoggerFactory.getLogger(DistinctEvent.class);

	protected abstract boolean isConditionMeet(DistinctEvent other);

	public Integer priority = 0;
}
