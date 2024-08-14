package net.client.event;

import io.netty.channel.ChannelHandler;

public interface RegisterEvent {
	void register(ChannelHandler channelHandler);
}
