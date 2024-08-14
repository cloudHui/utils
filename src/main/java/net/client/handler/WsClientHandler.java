package net.client.handler;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import net.channel.ChannelAttr;
import net.client.Sender;
import net.client.event.CloseEvent;
import net.client.event.RegisterEvent;
import net.codec.HAProxyDecoder;
import net.handler.Handler;
import net.handler.Handlers;
import net.message.Maker;
import net.message.Makers;
import net.message.Parser;
import net.message.TCPMessage;
import net.message.Transfer;
import net.safe.Safe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WsClientHandler<T extends WsClientHandler, M> extends SimpleChannelInboundHandler implements Sender<T, M> {
	private static final Logger logger = LoggerFactory.getLogger(WsClientHandler.class);
	private static final ClientManager clientManager;
	private final long id;
	private Channel channel;
	private Safe safe;
	private Transfer transfer;
	private Parser parser;
	private Handlers handlers;
	private Maker maker;
	private static final byte[] DEFAULT_DATA;
	private RegisterEvent registerEvent;
	private CloseEvent closeEvent;

	public static WsClientHandler getClient(long id) {
		return clientManager.getClient(id);
	}

	public static InetSocketAddress getRemoteIP(WsClientHandler clientHandler) {
		if (null == clientHandler.channel) {
			return null;
		} else {
			Object obj = clientHandler.channel.attr(HAProxyDecoder.HAPROXY).get();
			if (obj instanceof String) {
				String data = (String) obj;
				logger.info("{} {}", clientHandler.channel, data);
				if (!data.isEmpty()) {
					String[] splData = data.split(" ");
					if (splData.length == 6) {
						return new InetSocketAddress(splData[2], Integer.parseInt(splData[4]));
					}
				}
			}

			return (InetSocketAddress) clientHandler.channel.remoteAddress();
		}
	}

	public WsClientHandler(Parser parser, Handlers handlers) {
		this(parser, handlers, Transfer::DEFAULT, Makers.getMaker());
	}

	public WsClientHandler(Parser parser, Handlers handlers, Transfer transfer) {
		this(parser, handlers, transfer, Makers.getMaker());
	}

	public WsClientHandler(Parser parser, Handlers handlers, Transfer transfer, Maker maker) {
		this.safe = Safe::DEFAULT;
		this.id = clientManager.getId();
		this.parser = parser;
		this.handlers = handlers;
		this.transfer = transfer;
		this.maker = maker;
	}

	public final long getId() {
		return this.id;
	}

	public void setRegisterEvent(RegisterEvent registerEvent) {
		this.registerEvent = registerEvent;
	}

	public void setCloseEvent(CloseEvent closeEvent) {
		this.closeEvent = closeEvent;
	}

	public void setSafe(Safe safe) {
		this.safe = safe;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		if (null != this.registerEvent) {
			try {
				this.registerEvent.register(this);
			} catch (Exception e) {
				logger.error("[{}] failed for register event", ctx.channel(), e);
			}
		}

		this.channel = ctx.channel();
		ChannelAttr.setId(this.channel, this.getId());
		clientManager.addClient(this);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		logger.error("[{}] close", ctx.channel());
		clientManager.removeClient(this);
		if (null != this.closeEvent) {
			try {
				this.closeEvent.onClose(this);
			} catch (Exception e) {
				logger.error("[{}] failed for close event", ctx.channel(), e);
			}
		}

	}

	public void closeChannel() {
		clientManager.removeClient(this);
		if (null != this.channel) {
			try {
				this.channel.close();
			} catch (Exception e) {
				logger.error("[{}] force close channel", this.channel, e);
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
		if (IdleStateEvent.class.isAssignableFrom(event.getClass())) {
			IdleStateEvent idle = (IdleStateEvent) event;
			if (IdleState.READER_IDLE == idle.state()) {
				ctx.channel().close();
			}
		}

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof WebSocketFrame) {
			logger.error(msg.toString());
		}
	}

	@Override
	public void sendMessage(int msgId, Message msg, Map<Long, String> attachments) {
		this.channel.writeAndFlush(this.maker.wrap(msgId, msg, attachments));
	}

	@Override
	public void sendMessage(int msgId, Message msg, Map<Long, String> attachments, int mapId) {
		this.channel.writeAndFlush(this.maker.wrap(msgId, msg, attachments, mapId));
	}

	@Override
	public void sendMessage(int msgId, ByteString msg, Map<Long, String> attachments) {
		this.channel.writeAndFlush(this.maker.wrap(msgId, msg, attachments));
	}

	@Override
	public void sendMessage(int sequence, Integer msgId, Message message, Map<Long, String> attachments) {
		if (0 == sequence) {
			this.sendMessage(msgId, message, attachments);
		} else {
			this.channel.writeAndFlush(this.maker.wrap(sequence, msgId, message, attachments));
		}

	}

	@Override
	public void sendMessage(M msg) {
		this.channel.writeAndFlush(msg);
	}

	private void processTCPMessage(TCPMessage tcpMessage) {
		try {
			if (!this.safe.isValid(this, tcpMessage)) {
				logger.error("[{}] ERROR! {} is not safe message id", this.channel, String.format("0x%08x", tcpMessage.getMessageId()));
				this.channel.close();
				return;
			}

			if (this.transfer.isTransfer(this, tcpMessage)) {
				return;
			}

			Message msg;
			if (null != tcpMessage.getMessage() && tcpMessage.getMessage().length > 0) {
				msg = this.parser.parser(tcpMessage.getMessageId(), tcpMessage.getMessage());
			} else {
				msg = this.parser.parser(tcpMessage.getMessageId(), DEFAULT_DATA);
			}


			Handler handler = this.handlers.getHandler(tcpMessage.getMessageId());
			if (null == handler) {
				logger.error("[{}] ERROR! can not find handler for message({})", this.channel, String.format("0x%08x", tcpMessage.getMessageId()));
				return;
			}

			long now = System.currentTimeMillis();
			boolean close = handler.handler(this, (long) tcpMessage.getRoleId(), msg, tcpMessage.getMapId());
			now = System.currentTimeMillis() - now;
			if (now > 1000L) {
				logger.error("client handler:{} cost too long:{}ms", handler.getClass().getSimpleName(), now);
			} else {
				logger.warn("client handler:{} cost:{}ms", handler.getClass().getSimpleName(), now);
			}
			if (close) {
				return;
			}

			this.channel.close();
		} catch (Exception var4) {
			logger.error("[{}] ERROR! failed for process message({})", this.channel, String.format("0x%08x", tcpMessage.getMessageId()), var4);
		}

	}

	static {
		clientManager = ClientManager.INSTANCE;
		DEFAULT_DATA = "".getBytes();
	}

	private static class ClientManager {
		private static final ClientManager INSTANCE = new ClientManager();
		private final AtomicLong ID = new AtomicLong(0L);
		private final Map<Long, WsClientHandler> clientMap = new ConcurrentHashMap<>(4096);

		private ClientManager() {
		}

		private long getId() {
			return this.ID.incrementAndGet();
		}

		private void addClient(WsClientHandler client) {
			this.clientMap.put(client.getId(), client);
		}

		private void removeClient(WsClientHandler client) {
			this.removeClient(client.getId());
		}

		private void removeClient(long id) {
			this.clientMap.remove(id);

		}

		private WsClientHandler getClient(long id) {
			return this.clientMap.get(id);
		}
	}
}
