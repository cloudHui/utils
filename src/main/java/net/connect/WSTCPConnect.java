package net.connect;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import net.client.event.EventHandle;
import net.codec.WSTCPMessageDecoder;
import net.codec.WSTCPMessageEncoder;
import net.connect.handle.ConnectHandler;
import net.handler.Handlers;
import net.message.Parser;
import net.message.TCPMaker;
import net.message.Transfer;

@Sharable
public class WSTCPConnect extends ConnectHandler {
	private final EventLoopGroup eventLoopGroup;
	private final SocketAddress socketAddress;
	private final int retryInterval;

	public WSTCPConnect(EventLoopGroup eventLoopGroup, SocketAddress socketAddress, Transfer transfer, Parser parser, Handlers handlers, EventHandle eventHandle) {
		this(eventLoopGroup, socketAddress, 3, transfer, parser, handlers, eventHandle);
	}

	public WSTCPConnect(EventLoopGroup eventLoopGroup, SocketAddress socketAddress, int retryInterval, Transfer transfer, Parser parser, Handlers handlers, EventHandle eventHandle) {
		super(transfer, parser, handlers, TCPMaker.INSTANCE);
		this.eventLoopGroup = eventLoopGroup;
		this.socketAddress = socketAddress;
		this.retryInterval = retryInterval;
		this.setActiveEvent(eventHandle);
	}

	public String getIP() {
		return ((InetSocketAddress) this.socketAddress).getAddress().getHostAddress();
	}

	public int getPort() {
		return ((InetSocketAddress) this.socketAddress).getPort();
	}

	public WSTCPConnect connect() {
		connect(this.eventLoopGroup, this.socketAddress, new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new IdleStateHandler(1, 1, 1));
				p.addLast(new WSTCPMessageDecoder());
				p.addLast(new WSTCPMessageEncoder());
				p.addLast(WSTCPConnect.this);
			}
		});
		return this;
	}
}
