package net.service;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import net.client.ClientFactory;
import net.codec.TCPMessageDecoder;
import net.codec.TCPMessageEncoder;

import java.util.ArrayList;
import java.util.List;

public class TCPServiceHandler extends ChannelInitializer<SocketChannel> {
	private ClientFactory clientFactory;
	private int idleTime;

	public TCPServiceHandler(ClientFactory clientFactory) {
		this(0, clientFactory);
	}

	public TCPServiceHandler(int idleTime, ClientFactory clientFactory) {
		this.idleTime = idleTime;
		this.clientFactory = clientFactory;
	}

	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline p = socketChannel.pipeline();
		p.addLast("encoder", new TCPMessageEncoder());
		p.addLast("decoder", new TCPMessageDecoder());
		if (this.idleTime > 0) {
			p.addLast(new IdleStateHandler(this.idleTime, this.idleTime, this.idleTime));
		}

		for (ChannelHandlerAdapter c : this.clientFactory.create(socketChannel)) {
			p.addLast(c);
		}

	}

	public ClientFactory getClientFactory() {
		return getClientFactory(this.idleTime, this.clientFactory);
	}

	static ClientFactory getClientFactory(int idleTime, ClientFactory clientFactory) {
		return (channel) -> {
			List<ChannelHandlerAdapter> channels = new ArrayList<>(8);
			channels.add(new TCPMessageEncoder());
			channels.add(new TCPMessageDecoder());
			if (idleTime > 0) {
				channels.add(new IdleStateHandler(idleTime, idleTime, idleTime));
			}

			channels.addAll(clientFactory.create(channel));
			return channels;
		};
	}
}
