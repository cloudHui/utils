package net.client;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import net.message.TCPMessage;

public interface Sender {
	void sendMessage(int msgId, Message msg, long sequence);

	void sendMessage(int msgId, Message msg);

	void sendMessage(int msgId, Message msg, int mapId, long sequence);

	void sendMessage(int msgId, ByteString str, long sequence);

	void sendMessage(int msgId, int mapId, Message msg, long sequence);

	void sendMessage(TCPMessage msg);

	void sendMessage(int clientId, int msgId, int mapId, int resultId, Message msg, long sequence);
}
