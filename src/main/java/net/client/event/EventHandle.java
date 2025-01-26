package net.client.event;

import io.netty.channel.ChannelHandler;

public interface EventHandle {
	void handle(ChannelHandler channelHandler);
}
