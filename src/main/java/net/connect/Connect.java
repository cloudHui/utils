package net.connect;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import net.client.event.EventHandle;
import net.connect.handle.ConnectHandler;
import net.handler.Handlers;
import net.message.Parser;
import net.message.Transfer;
import net.proto.SysProto;

@Sharable
public class Connect extends ConnectHandler {
	private final EventLoopGroup eventLoopGroup;
	private final SocketAddress socketAddress;

	/**
	 * @param eventHandle 链接成功后触发的事件
	 */
	public Connect(EventLoopGroup eventLoopGroup, SocketAddress socketAddress, Transfer transfer, Parser parser, Handlers handlers, EventHandle eventHandle, EventHandle close) {
		super(transfer, parser, handlers);
		this.eventLoopGroup = eventLoopGroup;
		this.socketAddress = socketAddress;
		setActiveEvent(eventHandle);
		setCloseEvent(close);
	}

	public String getIP() {
		return ((InetSocketAddress) this.socketAddress).getAddress().getHostAddress();
	}

	public int getPort() {
		return ((InetSocketAddress) this.socketAddress).getPort();
	}

	public Connect connect() {
		connect(this.eventLoopGroup, this.socketAddress, new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new IdleStateHandler(0, 60, 0));
				p.addLast(new ProtobufVarint32LengthFieldPrepender());
				p.addLast(new ProtobufEncoder());
				p.addLast(new ProtobufVarint32FrameDecoder());
				p.addLast(new ProtobufDecoder(SysProto.SysMessage.getDefaultInstance()));
				p.addLast(Connect.this);
			}
		});
		return this;
	}
}
