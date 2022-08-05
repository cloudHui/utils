package net.message;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;

import java.util.Map;

public class TCPMaker implements Maker<TCPMessage> {
	public static final TCPMaker INSTANCE = new TCPMaker();

	public TCPMaker() {
	}

	public String version() {
		return "1.0";
	}

	public TCPMessage wrap(Integer msgId, Message msg, Map<Long, String> attachments) {
		return TCPMessage.newInstance(0, msgId, 0, msg.toByteArray());
	}

	public TCPMessage wrap(Long sequence, Integer msgId, Message msg, Map<Long, String> attachments) {
		return TCPMessage.newInstance(0, msgId, null == sequence ? 0 : sequence.intValue(), msg.toByteArray());
	}

	public TCPMessage wrap(Integer msgId, ByteString msg, Map<Long, String> attachments) {
		return TCPMessage.newInstance(0, msgId, 0, msg.toByteArray());
	}
}
