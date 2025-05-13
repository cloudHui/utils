package http.handler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import http.Linker;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;

public interface Handler<T> {
	String path();

	T parser(String msg);

	boolean handler(Linker linker, T t, String remote);

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
