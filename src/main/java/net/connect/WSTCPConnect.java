package net.connect;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import net.client.event.RegisterEvent;
import net.codec.WSTCPMessageDecoder;
import net.codec.WSTCPMessageEncoder;
import net.handler.Handlers;
import net.message.Parser;
import net.message.TCPMaker;
import net.message.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Sharable
public class WSTCPConnect extends ConnectHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(WSTCPConnect.class);
	private final EventLoopGroup eventLoopGroup;
	private final SocketAddress socketAddress;
	private final int retryInterval;

	public WSTCPConnect(EventLoopGroup eventLoopGroup, SocketAddress socketAddress, Transfer transfer, Parser parser, Handlers handlers, RegisterEvent registerEvent) {
		this(eventLoopGroup, socketAddress, 3, transfer, parser, handlers, registerEvent);
	}

	public WSTCPConnect(EventLoopGroup eventLoopGroup, SocketAddress socketAddress, int retryInterval, Transfer transfer, Parser parser, Handlers handlers, RegisterEvent registerEvent) {
		super(transfer, parser, handlers, TCPMaker.INSTANCE);
		this.eventLoopGroup = eventLoopGroup;
		this.socketAddress = socketAddress;
		this.retryInterval = retryInterval;
		this.setRegisterEvent(registerEvent);
	}

	public String getIP() {
		return ((InetSocketAddress) this.socketAddress).getAddress().getHostAddress();
	}

	public int getPort() {
		return ((InetSocketAddress) this.socketAddress).getPort();
	}

	public WSTCPConnect connect(int disconnectRetry) {
		Connect.connect(this.eventLoopGroup, this.socketAddress, this.retryInterval, new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new IdleStateHandler(0, 60, 0));
				p.addLast(new WSTCPMessageDecoder());
				p.addLast(new WSTCPMessageEncoder());
				p.addLast(WSTCPConnect.this);
			}
		}, disconnectRetry);
		return this;
	}
}
