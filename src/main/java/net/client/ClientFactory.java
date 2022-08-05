package net.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;

import java.util.List;

public interface ClientFactory {
	List<ChannelHandlerAdapter> create(Channel var1);
}
