package net.handler;

import com.google.protobuf.Message;
import net.client.Sender;

public interface Handler<T extends Message> {
	boolean handler(Sender sender, Long sequence, T var3, int mapId);
}
