package net.service;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import net.client.ClientFactory;
import net.codec.HAProxyDecoder;
import net.codec.TCPMessageDecoder;
import net.codec.TCPMessageEncoder;
import net.handler.IdleHandler;

public class TCPHAProxyServiceHandler extends ChannelInitializer<SocketChannel> {
	private ClientFactory clientFactory;
	private int idleTime;

	public TCPHAProxyServiceHandler(ClientFactory clientFactory) {
		this(0, clientFactory);
	}

	public TCPHAProxyServiceHandler(int idleTime, ClientFactory clientFactory) {
		this.idleTime = idleTime;
		this.clientFactory = clientFactory;
	}

	@Override
	protected void initChannel(SocketChannel socketChannel) {
		ChannelPipeline p = socketChannel.pipeline();
		p.addLast("haproxy", new HAProxyDecoder());
		p.addLast("encoder", new TCPMessageEncoder());
		p.addLast("decoder", new TCPMessageDecoder());
		if (this.idleTime > 0) {
			p.addLast(new IdleStateHandler(this.idleTime, this.idleTime, this.idleTime));
			p.addLast(new IdleHandler());
		}

		for (ChannelHandlerAdapter c : this.clientFactory.create(socketChannel)) {
			p.addLast(c);
		}

	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}
}
