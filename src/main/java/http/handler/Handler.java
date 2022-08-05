package http.handler;

import http.Linker;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public interface Handler<T> {
	String path();

	T parser(String msg);

	boolean handler(Linker linker, String path, String function, T t);

	default T paras(String msg, T t) {
		String deData = null;
		try {
			deData = URLDecoder.decode(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}


		assert deData != null;
		String[] keyValues = deData.split("&");
		for (String kv : keyValues) {
			String[] keyValue = kv.split("=");
			ReflectUtils.setValue(t, keyValue[0], keyValue[1]);

		}
		return t;
	}
}
