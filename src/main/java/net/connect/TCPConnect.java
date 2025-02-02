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
import net.codec.TCPMessageDecoder;
import net.codec.TCPMessageEncoder;
import net.connect.handle.ConnectHandler;
import net.handler.Handlers;
import net.message.Parser;
import net.message.TCPMaker;
import net.message.Transfer;

@Sharable
public class TCPConnect extends ConnectHandler {
	private final EventLoopGroup eventLoopGroup;
	private final SocketAddress socketAddress;

	public TCPConnect(EventLoopGroup eventLoopGroup, SocketAddress socketAddress, Transfer transfer, Parser parser, Handlers handlers, EventHandle eventHandle, EventHandle close) {
		super(transfer, parser, handlers, TCPMaker.INSTANCE);
		this.eventLoopGroup = eventLoopGroup;
		this.socketAddress = socketAddress;
		setActiveEvent(eventHandle);
		setCloseEvent(close);
	}

	public String getIP() {
		return ((InetSocketAddress) socketAddress).getAddress().getHostAddress();
	}

	public int getPort() {
		return ((InetSocketAddress) socketAddress).getPort();
	}

	public TCPConnect connect() {
		connect(eventLoopGroup, socketAddress, new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new IdleStateHandler(0, 60, 0));
				p.addLast(new TCPMessageEncoder());
				p.addLast(new TCPMessageDecoder());
				p.addLast(TCPConnect.this);
			}
		});
		return this;
	}

	@Override
	public String toString() {
		return "TCPConnect{" + "connectServer=" + getConnectServer() + ", localServer=" + getLocalServer() + '}';
	}
}
