package net.client;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;

import java.util.Map;

public interface Sender<T, M> {
	void sendMessage(Integer var1, Message var2, Map<Long, String> var3);

	void sendMessage(Integer var1, ByteString var2, Map<Long, String> var3);

	void sendMessage(Long var1, Integer var2, Message var3, Map<Long, String> var4);

	void sendMessage(M var1);
}
