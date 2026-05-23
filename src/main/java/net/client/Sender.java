package net.client;

import java.util.concurrent.CompletableFuture;

import com.google.protobuf.Message;
import net.message.TCPMessage;

public interface Sender {
	void sendMessage(int msgId, Message msg, int sequence);

	void sendMessage(TCPMessage msg);

	void sendMessage(int clientId, int msgId, long mapId, Message msg, int sequence);

	CompletableFuture<TCPMessage> sendMessageBackTcp(Message msg, int msgId, int timeout);

	CompletableFuture<TCPMessage> sendTcpMessage(TCPMessage msg, int timeout);
}
