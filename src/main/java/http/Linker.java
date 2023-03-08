package http;

public interface Linker {
	String remoteIp();

	<T> void sendMessage(int msgId, T msg);

	<T> void sendMessage(T msg);

	void sendMessage(String msg);
}
