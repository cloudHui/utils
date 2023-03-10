package net.client;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;

import java.util.Map;

public interface Sender<T, M> {
	void sendMessage(int msgId, Message var2, Map<Long, String> var3);

	void sendMessage(int msgId, Message var2, Map<Long, String> var3,int mapId);

	void sendMessage(int msgId, ByteString var2, Map<Long, String> var3);

	void sendMessage(int var1, Integer var2, Message var3, Map<Long, String> var4);

	void sendMessage(M var1);
}
