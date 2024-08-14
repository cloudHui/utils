package net.message;

import java.util.Map;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;

public interface Maker {

	TCPMessage wrap(int msgId, Message msg, Map<Long, String> attach);

	TCPMessage wrap(int msgId, Message msg, Map<Long, String> attach, int mapId);

	TCPMessage wrap(int roleId, int msgId, Message msg, Map<Long, String> attach);

	TCPMessage wrap(int var1, ByteString msgByte, Map<Long, String> attach);

	TCPMessage wrap(int roleId, int msgId, int mapId, int resultId, Message msg);
}
