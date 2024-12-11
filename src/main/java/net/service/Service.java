package net.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Service {
	private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);
	private final EventLoopGroup bossGroup;
	private final EventLoopGroup workerGroup;
	private final List<Channel> channels = new ArrayList<>();

	public Service(EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
		this.bossGroup = bossGroup;
		this.workerGroup = workerGroup;
	}

	public void start(ChannelHandler channelHandler, List<SocketAddress> socketAddresses) {
		ServerBootstrap serverBootstrap = ((((new ServerBootstrap()).group(this.bossGroup, this.workerGroup)
				.channel(NioServerSocketChannel.class))
				.option(ChannelOption.SO_BACKLOG, 1024))
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childOption(ChannelOption.TCP_NODELAY, true)
				.handler(new LoggingHandler(LogLevel.INFO)))
				.childHandler(channelHandler);
		ChannelFuture f;

		for (SocketAddress socketAddress : socketAddresses) {
			try {
				f = serverBootstrap.bind(socketAddress).sync();
				this.channels.add(f.channel());
			} catch (InterruptedException var8) {
				LOGGER.error("", var8);
			}
		}

	}

	public void destroy() {
		if (!this.channels.isEmpty()) {
			this.channels.forEach((channel) -> {
				try {
					channel.close().syncUninterruptibly();
				} catch (Exception var2) {
					LOGGER.error("", var2);
				}

			});
		}
	}
}
