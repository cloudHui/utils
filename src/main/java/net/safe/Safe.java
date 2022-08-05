package net.safe;

public interface Safe<T, M> {
	boolean isValid(T var1, M var2);

	static <T, M> boolean DEFAULT(T t, M msg) {
		return true;
	}
}
