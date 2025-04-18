package net.message;

import com.google.protobuf.Message;

public class TCPMaker {
	public static final TCPMaker INSTANCE = new TCPMaker();

	public TCPMaker() {
	}

	public TCPMessage wrap(int msgId, Message msg, long sequence) {
		return TCPMessage.newInstance(msgId, msg.toByteArray(), sequence);
	}

	public TCPMessage wrap(int clientId, int msgId, int mapId, int resultId, Message msg, long sequence) {
		return TCPMessage.newInstance(resultId, msgId, clientId, msg.toByteArray(), mapId, sequence);
	}
}
