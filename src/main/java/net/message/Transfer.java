package net.message;

import io.netty.channel.ChannelHandler;

public interface Transfer {
	boolean isTransfer(ChannelHandler connect, TCPMessage message) throws Exception;

	static boolean DEFAULT() {
		return false;
	}
}
