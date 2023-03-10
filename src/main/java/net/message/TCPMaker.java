package net.message;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;

import java.util.Map;

public class TCPMaker implements Maker<TCPMessage> {
	public static final TCPMaker INSTANCE = new TCPMaker();

	public TCPMaker() {
	}

	@Override
	public String version() {
		return "1.0";
	}

	@Override
	public TCPMessage wrap(int msgId, Message msg, Map<Long, String> attachments) {
		return TCPMessage.newInstance(0, msgId, 0, msg.toByteArray());
	}

	@Override
	public TCPMessage wrap(int msgId, Message msg, Map<Long, String> attachments,int mapId) {
		return TCPMessage.newInstance(0, msgId, 0, msg.toByteArray(),mapId);
	}

	@Override
	public TCPMessage wrap(int sequence, Integer msgId, Message msg, Map<Long, String> attachments) {
		return TCPMessage.newInstance(0, msgId, sequence, msg.toByteArray());
	}

	@Override
	public TCPMessage wrap(int msgId, ByteString msg, Map<Long, String> attachments) {
		return TCPMessage.newInstance(0, msgId, 0, msg.toByteArray());
	}
}
