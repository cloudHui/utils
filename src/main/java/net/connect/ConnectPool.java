package net.connect;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.pool.ChannelHealthChecker;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.FixedChannelPool.AcquireTimeoutAction;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import net.client.ClientFactory;
import net.client.Sender;
import net.handler.Handlers;
import net.message.Maker;
import net.message.Parser;
import net.message.TCPMessage;
import net.message.Transfer;
import net.proto.SysProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectPool<M> implements Sender {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectPool.class);
	private final SocketAddress socketAddress;
	private final ClientFactory clientFactory;
	private final EventLoopGroup eventLoopGroup;
	private ChannelPool pool;
	private final int maxSize;
	private final Maker maker;

	public ConnectPool(int maxSize, SocketAddress socketAddress, EventLoopGroup eventLoopGroup, Maker maker, Transfer transfer, Parser parser, Handlers handlers) {
		this(maxSize, socketAddress, (channel) -> {
			List<ChannelHandlerAdapter> h = new ArrayList<>();
			h.add(new ProtobufVarint32LengthFieldPrepender());
			h.add(new ProtobufEncoder());
			h.add(new IdleStateHandler(0, 180, 0));
			h.add(new ProtobufVarint32FrameDecoder());
			h.add(new ProtobufDecoder(SysProto.SysMessage.getDefaultInstance()));
			h.add(new ConnectHandler(transfer, parser, handlers));
			return h;
		}, eventLoopGroup, maker);
	}

	public ConnectPool(int maxSize, SocketAddress socketAddress, ClientFactory clientFactory, EventLoopGroup eventLoopGroup, Maker maker) {
		this.maxSize = maxSize;
		this.socketAddress = socketAddress;
		this.clientFactory = clientFactory;
		this.eventLoopGroup = eventLoopGroup;
		this.maker = maker;
	}

	public void initPool() {
		Bootstrap bootstrap = (new Bootstrap()).group(this.eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.TCP_NODELAY, true).remoteAddress(this.socketAddress);
		this.pool = new FixedChannelPool(bootstrap, new InnerChannelPoolHandler(), ChannelHealthChecker.ACTIVE, AcquireTimeoutAction.NEW, 0L, this.maxSize, 2147483647);
	}

	@Override
	public void sendMessage(int msgId, Message msg, Map<Long, String> attachments) {
		Channel channel = null;

		try {
			channel = this.pool.acquire().get();
			channel.writeAndFlush(this.maker.wrap(msgId, msg, attachments));
		} catch (Exception var9) {
			LOGGER.error("id:{} msg:{}", msgId, msg.toString(), var9);
		} finally {
			if (null != channel) {
				this.pool.release(channel);
			}

		}

	}

	@Override
	public void sendMessage(int msgId, Message msg, Map<Long, String> attachments, int mapId) {
		Channel channel = null;

		try {
			channel = this.pool.acquire().get();
			channel.writeAndFlush(this.maker.wrap(msgId, msg, attachments, mapId));
		} catch (Exception var9) {
			LOGGER.error("id:{} msg:{}", msgId, msg.toString(), var9);
		} finally {
			if (null != channel) {
				this.pool.release(channel);
			}

		}
	}

	@Override
	public void sendMessage(int msgId, ByteString msg, Map<Long, String> attachments) {
		Channel channel = null;

		try {
			channel = this.pool.acquire().get();
			channel.writeAndFlush(this.maker.wrap(msgId, msg, attachments));
		} catch (Exception var9) {
			LOGGER.error("id:{} msg:{}", msgId, msg.toString(), var9);
		} finally {
			if (null != channel) {
				this.pool.release(channel);
			}

		}

	}

	@Override
	public void sendMessage(int roleId, int msgId, Message msg, Map<Long, String> attachments) {
		Channel channel = null;

		try {
			channel = this.pool.acquire().get();
			channel.writeAndFlush(this.maker.wrap(roleId, msgId, msg, attachments));
		} catch (Exception var10) {
			LOGGER.error("id:{} msg:{}", msgId, msg.toString(), var10);
		} finally {
			if (null != channel) {
				this.pool.release(channel);
			}

		}

	}

	@Override
	public void sendMessage(TCPMessage msg) {
		Channel channel = null;

		try {
			channel = this.pool.acquire().get();
			channel.writeAndFlush(msg);
		} catch (Exception var7) {
			LOGGER.error("{}", msg.toString(), var7);
		} finally {
			if (null != channel) {
				this.pool.release(channel);
			}

		}

	}

	@Override
	public void sendMessage(int roleId, int msgId, int mapId, int resultId, Message msg) {
		Channel channel = null;

		try {
			channel = this.pool.acquire().get();
			channel.writeAndFlush(this.maker.wrap(roleId, msgId, mapId, resultId, msg));
		} catch (Exception var10) {
			LOGGER.error("id:{} msg:{}", msgId, msg.toString(), var10);
		} finally {
			if (null != channel) {
				this.pool.release(channel);
			}

		}
	}

	class InnerChannelPoolHandler implements ChannelPoolHandler {
		InnerChannelPoolHandler() {
		}

		@Override
		public void channelReleased(Channel channel) {
		}

		@Override
		public void channelAcquired(Channel channel) {
		}

		@Override
		public void channelCreated(Channel channel) {
			ChannelPipeline p = channel.pipeline();
			for (ChannelHandlerAdapter c : ConnectPool.this.clientFactory.create(channel)) {
				p.addLast(c);
			}
		}
	}
}
