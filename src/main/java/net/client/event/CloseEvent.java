package net.client.event;

import io.netty.channel.ChannelHandler;

public interface CloseEvent {
	void onClose(ChannelHandler channelHandler);
}
