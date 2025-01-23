package net.client;

import java.util.Map;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import net.message.TCPMessage;

public interface Sender {
	void sendMessage(int msgId, Message msg, Map<Long, String> attach);

	void sendMessage(int msgId, Message msg, Map<Long, String> attach, int mapId, long sequence);

	void sendMessage(int msgId, ByteString str, Map<Long, String> attach, long sequence);

	void sendMessage(int msgId, int mapId, Message msg, Map<Long, String> attach, long sequence);

	void sendMessage(TCPMessage msg);

	void sendMessage(int roleId, int msgId, int mapId, int resultId, Message msg, long sequence);
}
