package http.handler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import http.Linker;

public interface Handler<T> {
	String path();

	T parser(String msg);

	/**
	 * http 请求处理
	 *
	 * @param linker 链接
	 * @param t      请求参数
	 * @return 是否保持链接
	 */
	boolean handler(Linker linker, T t);

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
