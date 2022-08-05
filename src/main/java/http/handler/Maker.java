package http.handler;

public interface Maker<T, R> {
	R wrap(int msgId, T t);

	R wrap(T t);

	R wrap(String content);
}
