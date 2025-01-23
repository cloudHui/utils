package net.message;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;

public class TCPMaker implements Maker {
	public static final TCPMaker INSTANCE = new TCPMaker();

	public TCPMaker() {
	}

	@Override
	public TCPMessage wrap(int msgId, Message msg, long sequence) {
		return TCPMessage.newInstance(msgId, msg.toByteArray(), sequence);
	}

	@Override
	public TCPMessage wrap(int msgId, Message msg) {
		return TCPMessage.newInstance(0, msgId, 0, msg.toByteArray());
	}

	@Override
	public TCPMessage wrap(int msgId, Message msg, int mapId, long sequence) {
		return TCPMessage.newInstance(0, msgId, 0, msg.toByteArray(), mapId, sequence);
	}

	@Override
	public TCPMessage wrap(int roleId, int msgId, Message msg, long sequence) {
		return TCPMessage.newInstance(0, msgId, roleId, msg.toByteArray(), sequence);
	}

	@Override
	public TCPMessage wrap(int msgId, ByteString msg, long sequence) {
		return TCPMessage.newInstance(0, msgId, 0, msg.toByteArray(), sequence);
	}

	@Override
	public TCPMessage wrap(int roleId, int msgId, int mapId, int resultId, Message msg, long sequence) {
		return TCPMessage.newInstance(resultId, msgId, roleId, msg.toByteArray(), mapId, sequence);
	}
}
