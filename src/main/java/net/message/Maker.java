package net.message;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;

public interface Maker {

	TCPMessage wrap(int msgId, Message msg, long sequence);

	TCPMessage wrap(int msgId, Message msg);

	TCPMessage wrap(int msgId, Message msg, int mapId, long sequence);

	TCPMessage wrap(int roleId, int msgId, Message msg, long sequence);

	TCPMessage wrap(int var1, ByteString msgByte, long sequence);

	TCPMessage wrap(int roleId, int msgId, int mapId, int resultId, Message msg, long sequence);
}
