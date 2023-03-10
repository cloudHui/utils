package net.service;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import net.client.ClientFactory;
import net.proto.SysProto;

import java.util.ArrayList;
import java.util.List;

public class SysMessageServiceHandler extends ChannelInitializer<SocketChannel> {
	private ClientFactory clientFactory;
	private int idleTime;

	public SysMessageServiceHandler(ClientFactory clientFactory) {
		this(0, clientFactory);
	}

	public SysMessageServiceHandler(int idleTime, ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		this.idleTime = idleTime;
	}

	@Override
	protected void initChannel(SocketChannel c) {
		ChannelPipeline p = c.pipeline();
		p.addLast(new ProtobufVarint32FrameDecoder());
		p.addLast(new ProtobufDecoder(SysProto.SysMessage.getDefaultInstance()));
		p.addLast(new ProtobufVarint32LengthFieldPrepender());
		p.addLast(new ProtobufEncoder());
		if (this.idleTime > 0) {
			p.addLast(new IdleStateHandler(this.idleTime, this.idleTime, this.idleTime));
		}

		for (ChannelHandlerAdapter channelInboundHandlerAdapter : this.clientFactory.create(c)) {
			p.addLast(channelInboundHandlerAdapter);
		}

	}

	public ClientFactory getClientFactory() {
		return (channel) -> {
			List<ChannelHandlerAdapter> channels = new ArrayList<>(16);
			channels.add(new ProtobufVarint32FrameDecoder());
			channels.add(new ProtobufDecoder(SysProto.SysMessage.getDefaultInstance()));
			channels.add(new ProtobufVarint32LengthFieldPrepender());
			channels.add(new ProtobufEncoder());
			if (this.idleTime > 0) {
				channels.add(new IdleStateHandler(this.idleTime, this.idleTime, this.idleTime));
			}

			channels.addAll(this.clientFactory.create(channel));
			return channels;
		};
	}
}
