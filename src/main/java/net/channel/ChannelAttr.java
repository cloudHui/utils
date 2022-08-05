package net.channel;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class ChannelAttr {
	private static final AttributeKey<Long> ID = AttributeKey.newInstance("ID");

	public ChannelAttr() {
	}

	public static void setId(Channel channel, long id) {
		channel.attr(ID).set(id);
	}

	public static long getId(Channel channel) {
		return channel.attr(ID).get();
	}
}
