package net.connect;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import net.client.event.RegisterEvent;
import net.handler.Handlers;
import net.message.Parser;
import net.message.Transfer;
import net.proto.SysProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

@Sharable
public class Connect<M> extends ConnectHandler<Connect, M> {
	private static final Logger LOGGER = LoggerFactory.getLogger(Connect.class);
	private final EventLoopGroup eventLoopGroup;
	private final SocketAddress socketAddress;
	private final int retryInterval;

	public Connect(EventLoopGroup eventLoopGroup, SocketAddress socketAddress, Transfer transfer,
	               Parser parser, Handlers handlers, RegisterEvent registerEvent) {
		this(eventLoopGroup, socketAddress, 3, transfer, parser, handlers, registerEvent);
	}

	public Connect(EventLoopGroup eventLoopGroup, SocketAddress socketAddress, int retryInterval,
	               Transfer transfer, Parser parser, Handlers handlers, RegisterEvent registerEvent) {
		super(transfer, parser, handlers);
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

	public Connect connect() {
		connect(this.eventLoopGroup, this.socketAddress, this.retryInterval, new ChannelInitializer<SocketChannel>() {
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

	public static void connect(EventLoopGroup eventLoopGroup, SocketAddress socketAddress, int retryInterval,
	                           ChannelInitializer channelInitializer) {
		Bootstrap bootstrap = ((new Bootstrap()).group(eventLoopGroup))
				.channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(channelInitializer);

		try {
			bootstrap.connect(socketAddress).addListener((ChannelFutureListener) f0 -> {
				InetSocketAddress saddr = (InetSocketAddress) socketAddress;
				if (f0.isSuccess()) {
					f0.channel().closeFuture().addListener((ChannelFutureListener) f1 -> {
						if (retryInterval >= 0) {
							f1.channel().eventLoop().schedule(() -> {
								connect(f1.channel().eventLoop(), socketAddress, retryInterval, channelInitializer);
							}, retryInterval, TimeUnit.SECONDS);
						}

					});
					LOGGER.info("connect {}:{} is success!!!", saddr.getAddress().getHostAddress(), saddr.getPort());
				} else {
					if (retryInterval >= 0) {
						f0.channel().eventLoop().schedule(() -> {
							connect(f0.channel().eventLoop(), socketAddress, retryInterval, channelInitializer);
						}, (long) retryInterval, TimeUnit.SECONDS);
					}

					LOGGER.error("failed for connect {}:{}!!!", saddr.getAddress().getHostAddress(), saddr.getPort());
				}

			}).sync();
		} catch (Exception var6) {
			LOGGER.error("", var6);
		}

	}
}
