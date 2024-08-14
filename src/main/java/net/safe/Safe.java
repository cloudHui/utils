package net.safe;

public interface Safe {
	boolean isValid(int msgId);

	static boolean DEFAULT() {
		return true;
	}
}
