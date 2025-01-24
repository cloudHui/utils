package net.client.handler;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import net.channel.ChannelAttr;
import net.client.Sender;
import net.client.event.CloseEvent;
import net.client.event.RegisterEvent;
import net.handler.Handler;
import net.handler.Handlers;
import net.message.Maker;
import net.message.Parser;
import net.message.TCPMaker;
import net.message.TCPMessage;
import net.message.Transfer;
import net.safe.Safe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends ChannelInboundHandlerAdapter implements Sender {
	private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
	private static final ClientManager clientManager;
	private final long id;
	private Channel channel;
	private Safe safe;
	private final Transfer transfer;
	private final Parser parser;
	private final Handlers handlers;
	private final Maker maker;
	private static final byte[] DEFAULT_DATA;
	private RegisterEvent registerEvent;
	private CloseEvent closeEvent;

	public static ClientHandler getClient(long id) {
		return clientManager.getClient(id);
	}

	public static InetSocketAddress getRemoteIP(ClientHandler clientHandler) {
		if (null == clientHandler.channel) {
			return null;
		} else {
			return (InetSocketAddress) clientHandler.channel.remoteAddress();
		}
	}

	public ClientHandler(Parser parser, Handlers handlers) {
		this(parser, handlers, (t, msg) -> Transfer.DEFAULT(), TCPMaker.INSTANCE);
	}

	public ClientHandler(Parser parser, Handlers handlers, Transfer transfer) {
		this(parser, handlers, transfer, TCPMaker.INSTANCE);
	}

	public ClientHandler(Parser parser, Handlers handlers, Transfer transfer, Maker maker) {
		this.safe = (msgId) -> Safe.DEFAULT();
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
	public void channelRead(ChannelHandlerContext ctx, Object object) {
		if (object instanceof TCPMessage) {
			this.processTCPMessage((TCPMessage) object);
		} else {
			ctx.fireChannelRead(object);
		}

	}

	@Override
	public void sendMessage(int msgId, Message msg, long sequence) {
		this.channel.writeAndFlush(this.maker.wrap(msgId, msg, sequence));
	}

	@Override
	public void sendMessage(int msgId, Message msg) {
		this.channel.writeAndFlush(this.maker.wrap(msgId, msg));
	}

	@Override
	public void sendMessage(int msgId, Message msg, int mapId, long sequence) {
		this.channel.writeAndFlush(this.maker.wrap(msgId, msg, mapId,sequence));
	}

	@Override
	public void sendMessage(int msgId, ByteString str, long sequence) {
		this.channel.writeAndFlush(this.maker.wrap(msgId, str,sequence));
	}

	@Override
	public void sendMessage(int roleId, int mapId, Message msg, long sequence) {
		this.channel.writeAndFlush(this.maker.wrap(roleId, mapId, msg,sequence));
	}

	@Override
	public void sendMessage(TCPMessage msg) {
		this.channel.writeAndFlush(msg);
	}

	@Override
	public void sendMessage(int clientId, int msgId, int mapId, int resultId, Message msg, long sequence) {
		this.channel.writeAndFlush(this.maker.wrap(clientId, msgId, mapId, resultId, msg,sequence));
	}

	private void processTCPMessage(TCPMessage tMsg) {
		try {
			if (!this.safe.isValid(tMsg.getMessageId())) {
				logger.error("[{}] ERROR! {} is not safe message id", this.channel, String.format("0x%08x", tMsg.getMessageId()));
				this.channel.close();
				return;
			}

			if (this.transfer.isTransfer(this, tMsg)) {
				return;
			}

			Message msg;
			if (null != tMsg.getMessage() && tMsg.getMessage().length > 0) {
				msg = this.parser.parser(tMsg.getMessageId(), tMsg.getMessage());
			} else {
				msg = this.parser.parser(tMsg.getMessageId(), DEFAULT_DATA);
			}


			Handler handler = this.handlers.getHandler(tMsg.getMessageId());
			if (null == handler) {
				logger.error("[{}] ERROR! can not find handler for message({})", this.channel, String.format("0x%08x", tMsg.getMessageId()));
				return;
			}

			long now = System.currentTimeMillis();
			boolean noCloseChannel = handler.handler(this, tMsg.getClientId(), msg, tMsg.getMapId(),tMsg.getSequence());
			now = System.currentTimeMillis() - now;
			if (now > 1000L) {
				logger.error("client handler:{} cost too long:{}ms", handler.getClass().getSimpleName(), now);
			} else {
				logger.debug("client handler:{} cost:{}ms", handler.getClass().getSimpleName(), now);
			}
			if (noCloseChannel) {
				return;
			}

			this.channel.close();
		} catch (Exception var4) {
			logger.error("[{}] ERROR! failed for process message({})", this.channel, String.format("0x%08x", tMsg.getMessageId()), var4);
		}

	}

	static {
		clientManager = ClientManager.INSTANCE;
		DEFAULT_DATA = "".getBytes();
	}

	private static class ClientManager {
		private static final ClientManager INSTANCE = new ClientManager();
		private final AtomicLong ID = new AtomicLong(0L);
		private final Map<Long, ClientHandler> clientMap = new ConcurrentHashMap<>(4096);

		private ClientManager() {
		}

		private long getId() {
			return this.ID.incrementAndGet();
		}

		private void addClient(ClientHandler client) {
			this.clientMap.put(client.getId(), client);
		}

		private void removeClient(ClientHandler client) {
			this.removeClient(client.getId());
		}

		private void removeClient(long id) {
			this.clientMap.remove(id);

		}

		private ClientHandler getClient(long id) {
			return this.clientMap.get(id);
		}
	}
}
