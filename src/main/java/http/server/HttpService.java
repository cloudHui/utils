package http.server;

import http.HttpDecoder;
import http.handler.HttpResponseMaker;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class HttpService {
	private final static Logger LOGGER = LoggerFactory.getLogger(HttpService.class);
	private final EventLoopGroup bossGroup, workerGroup;

	public HttpService() {
		this(new NioEventLoopGroup());
	}

	public HttpService(EventLoopGroup eventLoopGroup) {
		this(eventLoopGroup, eventLoopGroup);
	}

	public HttpService(EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
		this.bossGroup = bossGroup;
		this.workerGroup = workerGroup;
	}

	public <H extends HttpDecoder> HttpService start(SocketAddress socketAddress, Class<H> clazz, HttpResponseMaker maker) {
		ServerBootstrap service = new ServerBootstrap()
				.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childOption(ChannelOption.TCP_NODELAY, true)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.handler(new LoggingHandler(LogLevel.DEBUG))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						p.addLast(new HttpRequestDecoder());
						p.addLast(new HttpResponseEncoder());
						p.addLast(new HttpObjectAggregator(10 * 1024 * 1024));
						p.addLast(clazz.newInstance().setMaker(maker));
					}
				});

		try {
			// sync???????????????????????????this.await()???????????????
			ChannelFuture channelFuture = service.bind(socketAddress).sync();

			// ??????????????????????????????
			if (!channelFuture.isSuccess()) {
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
				throw new RuntimeException(channelFuture.cause());
			}
		} catch (InterruptedException e) {
			LOGGER.error("{}", socketAddress.toString(), e);
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

		return this;
	}
}
