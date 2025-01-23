package net.handler;

import com.google.protobuf.Message;
import net.client.Sender;

public interface Handler {
	boolean handler(Sender sender, int clientId, Message msg, int mapId, long sequence);
}
