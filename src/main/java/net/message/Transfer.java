package net.message;

public interface Transfer<T, M> {
	boolean isTransfer(T var1, M var2) throws Exception;

	static <T, M> boolean DEFAULT(T t, M msg) {
		return false;
	}
}
