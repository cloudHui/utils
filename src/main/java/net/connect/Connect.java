package net.connect;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

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

@Sharable
public class Connect<M> extends ConnectHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(Connect.class);
	private EventLoopGroup eventLoopGroup;
	private SocketAddress socketAddress;
	private int registerRetry;//注册重试

	private int disconnectRetry;//断链重试

	public Connect(EventLoopGroup eventLoopGroup, SocketAddress socketAddress, Transfer transfer,
	               Parser parser, Handlers handlers, RegisterEvent registerEvent, int registerRetry, int disconnectRetry) {
		this(eventLoopGroup, socketAddress, registerRetry, transfer, parser, handlers, registerEvent, disconnectRetry);
	}

	/**
	 * @param registerRetry 默认重试
	 * @param registerEvent 链接成功后触发的事件
	 */
	public Connect(EventLoopGroup eventLoopGroup, SocketAddress socketAddress, int registerRetry,
	               Transfer transfer, Parser parser, Handlers handlers, RegisterEvent registerEvent, int disconnectRetry) {
		super(transfer, parser, handlers);
		this.eventLoopGroup = eventLoopGroup;
		this.socketAddress = socketAddress;
		this.registerRetry = registerRetry;
		this.disconnectRetry = 0;
		this.setRegisterEvent(registerEvent);
	}

	public String getIP() {
		return ((InetSocketAddress) this.socketAddress).getAddress().getHostAddress();
	}

	public int getPort() {
		return ((InetSocketAddress) this.socketAddress).getPort();
	}

	public Connect connect() {
		connect(this.eventLoopGroup, this.socketAddress, this.registerRetry, new ChannelInitializer<SocketChannel>() {
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
		}, this.disconnectRetry);
		return this;
	}

	public static void connect(EventLoopGroup eventLoopGroup, SocketAddress socketAddress, int retryInterval,
	                           ChannelInitializer channelInitializer, int disconnectRetry) {
		Bootstrap bootstrap = ((new Bootstrap()).group(eventLoopGroup))
				.channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(channelInitializer);

		try {
			bootstrap.connect(socketAddress).addListener((ChannelFutureListener) channelFuture -> {
				InetSocketAddress sad = (InetSocketAddress) socketAddress;
				if (channelFuture.isSuccess()) {
					//链接成功被关闭了也会重试
					final int disRetry = disconnectRetry - 1;
					channelFuture.channel().closeFuture().addListener((ChannelFutureListener) f1 -> {
						if (disRetry > 0) {
							f1.channel().eventLoop().schedule(() -> {
								connect(f1.channel().eventLoop(), socketAddress, retryInterval, channelInitializer, disRetry);
							}, 3, TimeUnit.SECONDS);
						}

					});
					LOGGER.info("connect {}:{} is success!!!", sad.getAddress().getHostAddress(), sad.getPort());
				} else {
					//链接失败也会重试
					final int regRetry = retryInterval - 1;
					if (regRetry > 0) {
						channelFuture.channel().eventLoop().schedule(() -> {
							connect(channelFuture.channel().eventLoop(), socketAddress, regRetry, channelInitializer, disconnectRetry);
						}, 3, TimeUnit.SECONDS);
					}

					LOGGER.error("failed for connect {}:{}!!!", sad.getAddress().getHostAddress(), sad.getPort());
				}

			}).sync();
		} catch (Exception var6) {
			LOGGER.error("", var6);
		}

	}
}
