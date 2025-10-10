package net.connect.handle;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import net.client.Sender;
import net.client.event.EventHandle;
import net.connect.ServerInfo;
import net.handler.Handler;
import net.handler.Handlers;
import net.message.Parser;
import net.message.TCPMaker;
import net.message.TCPMessage;
import net.message.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网络连接处理器，负责管理TCP连接、消息发送和接收处理
 */
public class ConnectHandler extends ChannelInboundHandlerAdapter implements Sender {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectHandler.class);

	// 核心组件
	private Transfer transfer;
	private final Parser parser;
	private final Handlers handlers;
	private final TCPMaker maker;
	private final byte[] MSG_DEFAULT;

	// 事件处理器
	private Consumer<ConnectHandler> idleRunner;
	private EventHandle activeHandle;
	private EventHandle closeEvent;

	// 连接管理
	private CompleterGroup completerGroup;
	private Channel channel;
	private ServerInfo connectServer;
	private ServerInfo localServer;

	// 重连配置
	private boolean diRetry = false; // 断链重试
	private boolean conRetry = false; // 连接重试

	public ConnectHandler(Transfer transfer, Parser parser, Handlers handlers) {
		this(transfer, parser, handlers, TCPMaker.INSTANCE);
	}

	public ConnectHandler(Transfer transfer, Parser parser, Handlers handlers, TCPMaker maker) {
		this.transfer = (t, msg) -> Transfer.DEFAULT();
		this.MSG_DEFAULT = "".getBytes();

		if (null != transfer) {
			this.transfer = transfer;
		}

		this.parser = parser;
		this.handlers = handlers;
		this.maker = maker;
	}

	// ==================== 配置方法 ====================

	public void setDiRetry(boolean diRetry) {
		this.diRetry = diRetry;
	}

	public void setConRetry(boolean conRetry) {
		this.conRetry = conRetry;
	}

	public void setActiveEvent(EventHandle activeEvent) {
		this.activeHandle = activeEvent;
	}

	public void setCloseEvent(EventHandle closeEvent) {
		this.closeEvent = closeEvent;
	}

	public void setIdleRunner(Consumer<ConnectHandler> runner) {
		this.idleRunner = runner;
	}

	public void setConnectServer(ServerInfo connectServer) {
		this.connectServer = connectServer;
	}

	public ServerInfo getConnectServer() {
		return connectServer;
	}

	public ServerInfo getLocalServer() {
		return localServer;
	}

	public void setLocalServer(ServerInfo localServer) {
		this.localServer = localServer;
	}

	// ==================== 网络事件处理 ====================

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		this.channel = ctx.channel();
		this.completerGroup = new CompleterGroup(channel.eventLoop());

		if (null != activeHandle) {
			try {
				activeHandle.handle(this);
			} catch (Exception e) {
				LOGGER.error("[{}] 激活事件处理失败", ctx.channel(), e);
			}
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		if (completerGroup != null) {
			completerGroup.destroy();
			completerGroup = null;
		}
		channel = null;

		if (closeEvent != null) {
			try {
				closeEvent.handle(this);
			} catch (Exception e) {
				LOGGER.error("[{}] 关闭事件处理失败", ctx.channel(), e);
			}
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object event) {
		if (event instanceof IdleStateEvent) {
			LOGGER.debug("[心跳事件:{}]", ((IdleStateEvent) event).state());
			if (null != idleRunner) {
				ctx.channel().eventLoop().execute(() -> {
					try {
						idleRunner.accept(this);
					} catch (Exception exception) {
						LOGGER.error("[空闲事件处理失败]", exception);
					}
				});
			}
		} else {
			LOGGER.debug("[用户事件:{}]", event.getClass().getName());
		}
	}

	// ==================== 消息处理 ====================

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msgObj) {
		if (!(msgObj instanceof TCPMessage)) {
			ctx.fireChannelRead(msgObj);
			LOGGER.error("未知消息类型:{}", msgObj.getClass());
			return;
		}

		TCPMessage msg = (TCPMessage) msgObj;
		long startTime = System.currentTimeMillis();

		try {
			processTCPMessage(ctx, msg, startTime);
		} catch (Exception exception) {
			LOGGER.error("[{}] 处理TCP消息({})失败", ctx.channel(), Integer.toHexString(msg.getMessageId()), exception);
		}
	}

	/**
	 * 处理TCP消息
	 */
	private void processTCPMessage(ChannelHandlerContext ctx, TCPMessage msg, long startTime) throws Exception {
		// 消息转发检查
		if (transfer.isTransfer(this, msg)) {
			return;
		}

		// 异步回调处理
		if (msg.getSequence() != 0 && completerGroup != null) {
			handleAsyncResponse(ctx, msg);
		} else {
			// 同步处理器处理
			handleSyncMessage(ctx, msg);
		}

		// 性能监控
		logProcessTime(startTime, msg.getMessageId());
	}

	/**
	 * 消息解析
	 */
	private Message parseMessage(TCPMessage msg) throws InvalidProtocolBufferException {
		if (null != msg.getMessage() && msg.getMessage().length > 0) {
			return parser.parser(msg.getMessageId(), msg.getMessage());
		} else {
			return parser.parser(msg.getMessageId(), MSG_DEFAULT);
		}
	}

	private void handleAsyncResponse(ChannelHandlerContext ctx, TCPMessage msg) throws InvalidProtocolBufferException {
		Completer completer = completerGroup.popCompleter(msg.getSequence());
		if (null != completer) {
			completer.msg = parseMessage(msg);
			ctx.channel().eventLoop().execute(completer);
		} else {
			CompleterTcpMsg completerTcpMsg = completerGroup.popCompleterTcpMsg(msg.getSequence());
			if (null != completerTcpMsg) {
				completerTcpMsg.msg = msg;
				ctx.channel().eventLoop().execute(completerTcpMsg);
			} else {
				LOGGER.error("找不到消息({})的超时和回调 sequence:{} msg:{} tcp:{}",
						Integer.toHexString(msg.getMessageId()), msg.getSequence(),
						completerGroup.getSequences(),
						completerGroup.getTcpSequences());
			}
		}
	}

	private void handleSyncMessage(ChannelHandlerContext ctx, TCPMessage msg) throws InvalidProtocolBufferException {
		Handler handler = handlers.getHandler(msg.getMessageId());
		if (null != handler) {
			handler.handler(this, msg.getClientId(), parseMessage(msg), msg.getMapId(), msg.getSequence());
		} else {
			LOGGER.error("找不到消息({})的处理器", Integer.toHexString(msg.getMessageId()));
		}
	}

	private void logProcessTime(long startTime, int messageId) {
		long costTime = System.currentTimeMillis() - startTime;
		String hexMsgId = Integer.toHexString(messageId);

		if (costTime > 1000L) {
			LOGGER.error("消息:{} 处理耗时过长:{}ms", hexMsgId, costTime);
		} else {
			LOGGER.debug("消息:{} 处理耗时:{}ms", hexMsgId, costTime);
		}
	}

	// ==================== 消息发送方法 ====================

	@Override
	public void sendMessage(int msgId, Message msg, long sequence) {
		channel.writeAndFlush(maker.wrap(msgId, msg, sequence));
	}

	@Override
	public void sendMessage(TCPMessage msg) {
		channel.writeAndFlush(msg);
	}

	@Override
	public void sendMessage(int clientId, int msgId, int mapId, int resultId, Message msg, long sequence) {
		channel.writeAndFlush(maker.wrap(clientId, msgId, mapId, resultId, msg, sequence));
	}

	public void sendMessage(int msgId, Message msg) {
		channel.writeAndFlush(maker.wrap(msgId, msg, 0));
	}

	/**
	 * 发送任意消息对象
	 */
	public void sendMessage(Object obj) {
		channel.writeAndFlush(obj);
	}

	/**
	 * 发送消息并等待响应（带超时）
	 */
	public CompletableFuture<Message> sendMessage(Message msg, int msgId, int timeout) {
		long sequence = completerGroup.getSequence();
		Completer completer = new Completer(timeout);
		completerGroup.addCompleter(sequence, completer);
		sendMessage(msgId, msg, sequence);
		return completer;
	}

	/**
	 * 发送TCP消息并等待响应（带超时）
	 */
	public CompletableFuture<Message> sendMessage(TCPMessage msg, int timeout) {
		long sequence = completerGroup.getSequence();
		msg.setSequence(sequence);
		Completer completer = new Completer(timeout);
		completerGroup.addCompleter(sequence, completer);
		sendMessage(msg);
		return completer;
	}

	/**
	 * 发送TCP消息并等待TCP响应（带超时）
	 */
	public CompletableFuture<TCPMessage> sendTcpMessage(TCPMessage msg, int timeout) {
		long sequence = completerGroup.getSequence();
		msg.setSequence(sequence);
		CompleterTcpMsg completer = new CompleterTcpMsg(timeout);
		completerGroup.addCompleterTcpMsg(sequence, completer);
		sendMessage(msg);
		return completer;
	}

	// ==================== 连接管理 ====================

	/**
	 * 建立网络连接
	 */
	public void connect(EventLoopGroup eventLoopGroup, SocketAddress socketAddress,
						ChannelInitializer<SocketChannel> channelInitializer) {
		Bootstrap bootstrap = createBootstrap(eventLoopGroup, channelInitializer);

		try {
			bootstrap.connect(socketAddress).addListener(createConnectListener(socketAddress, channelInitializer)).sync();
		} catch (Exception e) {
			LOGGER.error("连接失败:{}", connectServer, e);
		}
	}

	private Bootstrap createBootstrap(EventLoopGroup eventLoopGroup,
									  ChannelInitializer<SocketChannel> channelInitializer) {
		return new Bootstrap()
				.group(eventLoopGroup)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(channelInitializer);
	}

	private ChannelFutureListener createConnectListener(SocketAddress socketAddress,
														ChannelInitializer<SocketChannel> channelInitializer) {
		return future -> {
			InetSocketAddress address = (InetSocketAddress) socketAddress;
			String hostPort = address.getHostName() + ":" + address.getPort();

			if (future.isSuccess()) {
				handleConnectSuccess(future, hostPort, channelInitializer);
			} else {
				handleConnectFailure(future, hostPort, channelInitializer);
			}
		};
	}

	private void handleConnectSuccess(io.netty.channel.ChannelFuture future, String hostPort,
									  ChannelInitializer<SocketChannel> channelInitializer) {
		if (diRetry) {
			// 连接成功后启用断链重连
			future.channel().closeFuture().addListener((ChannelFutureListener) closeFuture ->
					closeFuture.channel().eventLoop().schedule(
							() -> connect(closeFuture.channel().eventLoop(),
									future.channel().remoteAddress(), channelInitializer),
							3, TimeUnit.SECONDS));
		}
		LOGGER.info("[连接 {} 成功!!!]", hostPort);
	}

	private void handleConnectFailure(io.netty.channel.ChannelFuture future, String hostPort,
									  ChannelInitializer<SocketChannel> channelInitializer) {
		if (conRetry) {
			// 连接失败后启用重连
			future.channel().eventLoop().schedule(
					() -> connect(future.channel().eventLoop(),
							future.channel().remoteAddress(), channelInitializer),
					3, TimeUnit.SECONDS);
		}
		LOGGER.error("[连接 {} 失败!!!]", hostPort);
	}
}