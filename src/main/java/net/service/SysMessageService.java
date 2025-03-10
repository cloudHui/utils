package net.service;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import net.client.handler.ClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class SysMessageService extends Service {
	private static final Logger LOGGER = LoggerFactory.getLogger(TCPService.class);
	private final int idleTime;
	private final Class<? extends ClientHandler> clazz;

	public SysMessageService(Class<? extends ClientHandler> clazz) {
		this(0, clazz);
	}

	public SysMessageService(int idleTime, Class<? extends ClientHandler> clazz) {
		this(new NioEventLoopGroup(), idleTime, clazz);
	}

	public SysMessageService(EventLoopGroup eventLoopGroup, int idleTime, Class<? extends ClientHandler> clazz) {
		this(eventLoopGroup, eventLoopGroup, idleTime, clazz);
	}

	public SysMessageService(EventLoopGroup bossGroup, EventLoopGroup workerGroup, int idleTime, Class<? extends ClientHandler> clazz) {
		super(bossGroup, workerGroup);
		this.clazz = clazz;
		this.idleTime = idleTime;
	}

	public SysMessageService start(List<SocketAddress> socketAddresses) {
		super.start(new SysMessageServiceHandler(this.idleTime, (channel) -> {
			List<ChannelHandlerAdapter> channels = new ArrayList<>();

			try {
				channels.add(this.clazz.newInstance());
			} catch (Exception var4) {
				LOGGER.error("", var4);
			}

			return channels;
		}), socketAddresses);
		return this;
	}
}
