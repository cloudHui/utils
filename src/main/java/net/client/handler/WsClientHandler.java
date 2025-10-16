package net.client.handler;

import java.net.InetSocketAddress;

import com.google.protobuf.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.timeout.IdleState;
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

public class WsClientHandler extends SimpleChannelInboundHandler<WebSocketFrame> implements Sender {
	private static final Logger logger = LoggerFactory.getLogger(WsClientHandler.class);
	private static final SenderManager clientManager;
	private final int id;
	private Channel channel;
	private Safe safe;
	private final Transfer transfer;
	private final Parser parser;
	private final Handlers handlers;
	private final TCPMaker maker;
	private static final byte[] DEFAULT_DATA;
	private EventHandle activeHandle;
	private EventHandle closeEvent;

	public static WsClientHandler getClient(long id) {
		return (WsClientHandler) clientManager.getClient((int) id);
	}

	public static InetSocketAddress getRemoteIP(WsClientHandler clientHandler) {
		if (null == clientHandler.channel) {
			return null;
		} else {
			return (InetSocketAddress) clientHandler.channel.remoteAddress();
		}
	}

	public WsClientHandler(Parser parser, Handlers handlers) {
		this(parser, handlers, (t, msg) -> Transfer.DEFAULT(), TCPMaker.INSTANCE);
	}

	public WsClientHandler(Parser parser, Handlers handlers, Transfer transfer) {
		this(parser, handlers, transfer, TCPMaker.INSTANCE);
	}

	public WsClientHandler(Parser parser, Handlers handlers, Transfer transfer, TCPMaker maker) {
		this.safe = (msgId) -> Safe.DEFAULT();
		this.id = clientManager.getId();
		this.parser = parser;
		this.handlers = handlers;
		this.transfer = transfer;
		this.maker = maker;
	}

	public final int getId() {
		return this.id;
	}

	public void setActiveEvent(EventHandle eventHandle) {
		this.activeHandle = eventHandle;
	}

	public void setCloseEvent(EventHandle closeEvent) {
		this.closeEvent = closeEvent;
	}

	public void setSafe(Safe safe) {
		this.safe = safe;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		if (null != this.activeHandle) {
			try {
				this.activeHandle.handle(this);
			} catch (Exception e) {
				logger.error("[{}] failed for register event", ctx.channel(), e);
			}
		}

		this.channel = ctx.channel();
		ChannelAttr.setId(this.channel, this.getId());
		clientManager.addClient(this);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object object) {
		if (object instanceof TCPMessage) {
			processTCPMessage((TCPMessage) object);
		} else {
			logger.error("un handle msgType {}", object.getClass().getSimpleName());
			ctx.fireChannelRead(object);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		logger.error("[{}] close", ctx.channel());
		if (null != this.closeEvent) {
			try {
				this.closeEvent.handle(this);
			} catch (Exception e) {
				logger.error("[{}] failed for close event", ctx.channel(), e);
			}
		}
		closeChannel();
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
			closeChannel();
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
	public void sendMessage(int msgId, Message msg, int sequence) {
		channel.writeAndFlush(maker.wrap(msgId, msg, sequence));
	}

	@Override
	public void sendMessage(TCPMessage msg) {
		channel.writeAndFlush(msg);
	}

	@Override
	public void sendMessage(int clientId, int msgId, int mapId, Message msg, int sequence) {
		channel.writeAndFlush(maker.wrap(clientId, msgId, mapId, msg, sequence));
	}

	private void processTCPMessage(TCPMessage tMsg) {
		try {
			if (!safe.isValid(tMsg.getMessageId())) {
				logger.error("[{}] ERROR! {} is not safe message id", channel, Integer.toHexString(tMsg.getMessageId()));
				channel.close();
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
				logger.error("[{}] ERROR! can not find handler for message({})", channel, Integer.toHexString(tMsg.getMessageId()));
				return;
			}

			long now = System.currentTimeMillis();
			boolean noCloseChannel = handler.handler(this, tMsg.getClientId(), msg, tMsg.getMapId(), tMsg.getSequence());
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
			logger.error("[{}] ERROR! failed for process message({})", channel, Integer.toHexString(tMsg.getMessageId()), var4);
		}

	}

	static {
		clientManager = SenderManager.INSTANCE;
		DEFAULT_DATA = "".getBytes();
	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) {
		if (msg != null) {
			logger.error(msg.toString());
		}
	}

}
