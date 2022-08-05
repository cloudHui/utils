package net.service;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.timeout.IdleStateHandler;
import net.client.handler.ClientHandler;
import net.codec.WSTCPMessageDecoder;
import net.codec.WSTCPMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class TCPWSService extends Service {
	private static final Logger LOGGER = LoggerFactory.getLogger(TCPWSService.class);
	private final String webSocketPath;
	private final int idleTime;
	private final Class<? extends ClientHandler> clazz;

	public TCPWSService(String webSocketPath, Class<? extends ClientHandler> clazz) {
		this(webSocketPath, 0, clazz);
	}

	public TCPWSService(String webSocketPath, int idleTime, Class<? extends ClientHandler> clazz) {
		this(new NioEventLoopGroup(), webSocketPath, idleTime, clazz);
	}

	public TCPWSService(EventLoopGroup eventLoopGroup, String webSocketPath, int idleTime, Class<? extends ClientHandler> clazz) {
		this(eventLoopGroup, eventLoopGroup, webSocketPath, idleTime, clazz);
	}

	public TCPWSService(EventLoopGroup bossGroup, EventLoopGroup workerGroup, String webSocketPath, int idleTime, Class<? extends ClientHandler> clazz) {
		super(bossGroup, workerGroup);
		this.webSocketPath = webSocketPath;
		this.idleTime = idleTime;
		this.clazz = clazz;
	}

	public TCPWSService start(List<SocketAddress> socketAddresses) {
		super.start(new WSServiceHandler(this.webSocketPath, (channel) -> {
			List<ChannelHandlerAdapter> channels = new ArrayList<>(4);

			try {
				channels.add(new WSTCPMessageDecoder());
				channels.add(new WSTCPMessageEncoder());
				if (this.idleTime > 0) {
					channels.add(new IdleStateHandler(this.idleTime, this.idleTime, this.idleTime));
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
