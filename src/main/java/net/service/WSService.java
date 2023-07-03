package net.service;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.timeout.IdleStateHandler;
import net.client.handler.WsClientHandler;
import net.codec.WSTCPMessageDecoder;
import net.codec.WSTCPMessageEncoder;
import net.handler.IdleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WSService extends Service {
	private static final Logger LOGGER = LoggerFactory.getLogger(WSService.class);
	private final String webSocketPath;
	private final int idleTime;
	private final Class<? extends WsClientHandler> clazz;

	public WSService(String webSocketPath, Class<? extends WsClientHandler> clazz) {
		this(webSocketPath, 0, clazz);
	}

	public WSService(String webSocketPath, int idleTime, Class<? extends WsClientHandler> clazz) {
		this(new NioEventLoopGroup(), webSocketPath, idleTime, clazz);
	}

	public WSService(EventLoopGroup eventLoopGroup, String webSocketPath, int idleTime, Class<? extends WsClientHandler> clazz) {
		this(eventLoopGroup, eventLoopGroup, webSocketPath, idleTime, clazz);
	}

	public WSService(EventLoopGroup bossGroup, EventLoopGroup workerGroup, String webSocketPath, int idleTime, Class<? extends WsClientHandler> clazz) {
		super(bossGroup, workerGroup);
		this.webSocketPath = webSocketPath;
		this.idleTime = idleTime;
		this.clazz = clazz;
	}

	public WSService start(List<SocketAddress> socketAddresses) {
		super.start(new WSServiceHandler(this.webSocketPath, (channel) -> {
			List<ChannelHandlerAdapter> channels = new ArrayList<>(4);

			try {
				channels.add(new WSTCPMessageDecoder());
				channels.add(new WSTCPMessageEncoder());
				if (this.idleTime > 0) {
					channels.add(new IdleStateHandler(this.idleTime, this.idleTime, this.idleTime));
					channels.add(new IdleHandler());
				}

				channels.add(this.clazz.newInstance());
			} catch (Exception var4) {
				LOGGER.error("", var4);
			}

			return channels;
		}), socketAddresses);
		return this;
	}
}
