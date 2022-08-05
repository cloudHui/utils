package net.service;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import net.client.ClientFactory;

public class WSServiceHandler extends ChannelInitializer<SocketChannel> {
	private final String webSocketPath;
	private final ClientFactory clientFactory;

	public WSServiceHandler(String webSocketPath, ClientFactory clientFactory) {
		this.webSocketPath = webSocketPath;
		this.clientFactory = clientFactory;
	}

	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		p.addLast(new HttpRequestDecoder());
		p.addLast(new HttpObjectAggregator(65535));
		p.addLast(new WebSocketFrameAggregator(2147483647));
		p.addLast(new WebSocketServerProtocolHandler(this.webSocketPath));
		p.addLast(new HttpResponseEncoder());

		for (ChannelHandlerAdapter channelInboundHandlerAdapter : this.clientFactory.create(ch)) {
			p.addLast(channelInboundHandlerAdapter);
		}

	}
}
