package net.message;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;

import java.util.Map;

public interface Maker<T> {
	String version();

	T wrap(Integer var1, Message var2, Map<Long, String> var3);

	T wrap(Long var1, Integer var2, Message var3, Map<Long, String> var4);

	T wrap(Integer var1, ByteString var2, Map<Long, String> var3);
}
