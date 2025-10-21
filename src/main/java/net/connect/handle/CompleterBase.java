package net.connect.handle;

public interface CompleterBase extends Runnable{
	void setEx(Throwable ex);

	boolean isTimeout(long currentTime);
}