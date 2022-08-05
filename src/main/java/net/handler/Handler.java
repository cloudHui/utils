package net.handler;

import com.google.protobuf.Message;
import net.client.Sender;

public interface Handler<T extends Message> {
	boolean handler(Sender var1, Long var2, T var3);
}
