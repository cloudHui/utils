package net.service;

import java.net.SocketAddress;
import java.util.List;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import net.client.handler.ClientHandler;

public class ServerService extends TCPService {


	public ServerService(int idleTime, Class<? extends ClientHandler> clazz) {
		this(new NioEventLoopGroup(), idleTime, clazz);
	}

	public ServerService(EventLoopGroup eventLoopGroup, int idleTime, Class<? extends ClientHandler> clazz) {
		this(eventLoopGroup, eventLoopGroup, idleTime, clazz);
	}

	public ServerService(EventLoopGroup bossGroup, EventLoopGroup workGroup, int idleTime, Class<? extends ClientHandler> clazz) {
		super(bossGroup, workGroup, idleTime, clazz);
	}

	public ServerService start(List<SocketAddress> socketAddresses) {
		super.start(socketAddresses);
		return this;
	}
}
