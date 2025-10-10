package net.client.handler;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import net.channel.ChannelAttr;
import net.client.Sender;
import net.client.event.EventHandle;
import net.handler.Handler;
import net.handler.Handlers;
import net.message.Parser;
import net.message.TCPMaker;
import net.message.TCPMessage;
import net.message.Transfer;
import net.safe.Safe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端连接处理器，负责管理客户端连接、消息处理和发送
 */
public class ClientHandler extends ChannelInboundHandlerAdapter implements Sender {
	private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
	private static final SenderManager SENDER_MANAGER;
	private static final byte[] DEFAULT_DATA;

	static {
		SENDER_MANAGER = SenderManager.INSTANCE;
		DEFAULT_DATA = "".getBytes();
	}

	// 核心组件
	private final int id;
	private final Parser parser;
	private final Handlers handlers;
	private final Transfer transfer;
	private final TCPMaker maker;

	// 连接状态
	private Channel channel;
	private Safe safe;
	private EventHandle activeHandle;
	private EventHandle closeEvent;

	// ==================== 静态方法 ====================

	public static ClientHandler getClient(int id) {
		return (ClientHandler) SENDER_MANAGER.getClient(id);
	}

	public static InetSocketAddress getRemoteIP(ClientHandler clientHandler) {
		if (null == clientHandler.channel) {
			return null;
		} else {
			return (InetSocketAddress) clientHandler.channel.remoteAddress();
		}
	}

	public static Map<Integer, Sender> getAllClient() {
		return new HashMap<>(SENDER_MANAGER.clientMap);
	}

	// ==================== 构造函数 ====================

	public ClientHandler(Parser parser, Handlers handlers) {
		this(parser, handlers, (t, msg) -> Transfer.DEFAULT(), TCPMaker.INSTANCE);
	}

	public ClientHandler(Parser parser, Handlers handlers, Transfer transfer) {
		this(parser, handlers, transfer, TCPMaker.INSTANCE);
	}

	public ClientHandler(Parser parser, Handlers handlers, Transfer transfer, TCPMaker maker) {
		this.safe = (msgId) -> Safe.DEFAULT();
		this.id = SENDER_MANAGER.getId();
		this.parser = parser;
		this.handlers = handlers;
		this.transfer = transfer;
		this.maker = maker;
	}

	// ==================== 配置方法 ====================

	public int getId() {
		return id;
	}

	public void setActiveEvent(EventHandle eventHandle) {
		activeHandle = eventHandle;
	}

	public void setCloseEvent(EventHandle closeEvent) {
		this.closeEvent = closeEvent;
	}

	public void setSafe(Safe safe) {
		this.safe = safe;
	}

	// ==================== 网络事件处理 ====================

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		this.channel = ctx.channel();
		ChannelAttr.setId(channel, getId());
		SENDER_MANAGER.addClient(this);

		if (null != activeHandle) {
			try {
				activeHandle.handle(this);
			} catch (Exception e) {
				logger.error("[{}] 注册事件失败", ctx.channel(), e);
			}
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		logger.error("[{}] 连接关闭", ctx.channel());
		SENDER_MANAGER.removeClient(this);

		if (null != closeEvent) {
			try {
				closeEvent.handle(this);
			} catch (Exception e) {
				logger.error("[{}] 关闭事件处理失败", ctx.channel(), e);
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		Channel channel = ctx.channel();
		if (channel.isActive()) {
			channel.close();
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object event) {
		if (event instanceof IdleStateEvent) {
			logger.debug("[心跳事件:{}]", ((IdleStateEvent) event).state());
			ctx.channel().close();
		}
	}

	// ==================== 消息处理 ====================

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object object) {
		if (object instanceof TCPMessage) {
			processTCPMessage((TCPMessage) object);
		} else {
			ctx.fireChannelRead(object);
		}
	}

	/**
	 * 处理TCP消息
	 */
	private void processTCPMessage(TCPMessage tcpMsg) {
		try {
			if (!validateMessageSafety(tcpMsg)) {
				return;
			}

			if (transfer.isTransfer(this, tcpMsg)) {
				return;
			}

			Message message = parseMessageContent(tcpMsg);
			boolean shouldKeepChannelOpen = executeMessageHandler(tcpMsg, message);
			if (!shouldKeepChannelOpen) {
				channel.close();
			}
		} catch (Exception e) {
			logger.error("[{}] 处理消息({})失败", channel, Integer.toHexString(tcpMsg.getMessageId()), e);
		}
	}

	/**
	 * 验证消息安全性
	 */
	private boolean validateMessageSafety(TCPMessage tcpMsg) {
		if (!safe.isValid(tcpMsg.getMessageId())) {
			logger.error("[{}] 错误! {} 不是安全的消息ID", channel, Integer.toHexString(tcpMsg.getMessageId()));
			channel.close();
			return false;
		}
		return true;
	}

	/**
	 * 解析消息内容
	 */
	private Message parseMessageContent(TCPMessage tcpMsg) throws InvalidProtocolBufferException {
		if (null != tcpMsg.getMessage() && tcpMsg.getMessage().length > 0) {
			return parser.parser(tcpMsg.getMessageId(), tcpMsg.getMessage());
		} else {
			return parser.parser(tcpMsg.getMessageId(), DEFAULT_DATA);
		}
	}

	/**
	 * 执行消息处理器
	 */
	private boolean executeMessageHandler(TCPMessage tcpMsg, Message message) {
		Handler handler = handlers.getHandler(tcpMsg.getMessageId());
		if (null == handler) {
			logger.error("[{}] 错误! 找不到消息({})的处理器", channel, Integer.toHexString(tcpMsg.getMessageId()));
			return false;
		}

		long startTime = System.currentTimeMillis();
		boolean shouldKeepChannelOpen = handler.handler(this, tcpMsg.getClientId(), message, tcpMsg.getMapId(),
				tcpMsg.getSequence());
		logHandlerPerformance(handler, startTime);

		return shouldKeepChannelOpen;
	}

	/**
	 * 记录处理器性能
	 */
	private void logHandlerPerformance(Handler handler, long startTime) {
		long costTime = System.currentTimeMillis() - startTime;
		String handlerName = handler.getClass().getSimpleName();

		if (costTime > 1000L) {
			logger.error("客户端处理器:{} 耗时过长:{}ms", handlerName, costTime);
		} else {
			logger.debug("客户端处理器:{} 耗时:{}ms", handlerName, costTime);
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

	// ==================== 连接管理 ====================

	/**
	 * 强制关闭通道
	 */
	public void closeChannel() {
		SENDER_MANAGER.removeClient(this);
		if (null != channel) {
			try {
				channel.close();
			} catch (Exception e) {
				logger.error("[{}] 强制关闭通道失败", channel, e);
			}
		}
	}
}