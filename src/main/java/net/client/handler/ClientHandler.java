package net.client.handler;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.protobuf.ByteString;
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
	private final int id;
	private Channel channel;
	private Safe safe;
	private final Transfer transfer;
	private final Parser parser;
	private final Handlers handlers;
	private final Maker maker;
	private static final byte[] DEFAULT_DATA;
	private EventHandle activeHandle;
	private EventHandle closeEvent;

	public static ClientHandler getClient(int id) {
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
		safe = (msgId) -> Safe.DEFAULT();
		id = clientManager.getId();
		this.parser = parser;
		this.handlers = handlers;
		this.transfer = transfer;
		this.maker = maker;
	}

	public int getId() {
		return id;
	}

	public static Map<Integer, ClientHandler> getAllClient() {
		return new HashMap<>(ClientHandler.clientManager.clientMap);
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

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		if (null != activeHandle) {
			try {
				activeHandle.handle(this);
			} catch (Exception e) {
				logger.error("[{}] failed for register event", ctx.channel(), e);
			}
		}

		channel = ctx.channel();
		ChannelAttr.setId(channel, getId());
		clientManager.addClient(this);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		logger.error("[{}] close", ctx.channel());
		clientManager.removeClient(this);
		if (null != closeEvent) {
			try {
				closeEvent.handle(this);
			} catch (Exception e) {
				logger.error("[{}] failed for close event", ctx.channel(), e);
			}
		}
	}

	public void closeChannel() {
		clientManager.removeClient(this);
		if (null != channel) {
			try {
				channel.close();
			} catch (Exception e) {
				logger.error("[{}] force close channel", channel, e);
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
			logger.debug("[userEventTriggered:{}]", ((IdleStateEvent) event).state());
			ctx.channel().close();
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object object) {
		if (object instanceof TCPMessage) {
			processTCPMessage((TCPMessage) object);
		} else {
			ctx.fireChannelRead(object);
		}

	}

	@Override
	public void sendMessage(int msgId, Message msg, long sequence) {
		channel.writeAndFlush(maker.wrap(msgId, msg, sequence));
	}

	@Override
	public void sendMessage(int msgId, Message msg) {
		channel.writeAndFlush(maker.wrap(msgId, msg));
	}

	@Override
	public void sendMessage(int msgId, Message msg, int mapId, long sequence) {
		channel.writeAndFlush(maker.wrap(msgId, msg, mapId, sequence));
	}

	@Override
	public void sendMessage(int msgId, ByteString str, long sequence) {
		channel.writeAndFlush(maker.wrap(msgId, str, sequence));
	}

	@Override
	public void sendMessage(int roleId, int mapId, Message msg, long sequence) {
		channel.writeAndFlush(maker.wrap(roleId, mapId, msg, sequence));
	}

	@Override
	public void sendMessage(TCPMessage msg) {
		channel.writeAndFlush(msg);
	}

	@Override
	public void sendMessage(int clientId, int msgId, int mapId, int resultId, Message msg, long sequence) {
		channel.writeAndFlush(maker.wrap(clientId, msgId, mapId, resultId, msg, sequence));
	}

	private void processTCPMessage(TCPMessage tMsg) {
		try {
			if (!safe.isValid(tMsg.getMessageId())) {
				logger.error("[{}] ERROR! {} is not safe message id", channel, String.format("0x%08x", tMsg.getMessageId()));
				channel.close();
				return;
			}

			if (transfer.isTransfer(this, tMsg)) {
				return;
			}

			Message msg;
			if (null != tMsg.getMessage() && tMsg.getMessage().length > 0) {
				msg = parser.parser(tMsg.getMessageId(), tMsg.getMessage());
			} else {
				msg = parser.parser(tMsg.getMessageId(), DEFAULT_DATA);
			}


			Handler handler = handlers.getHandler(tMsg.getMessageId());
			if (null == handler) {
				logger.error("[{}] ERROR! can not find handler for message({})", channel, String.format("0x%08x", tMsg.getMessageId()));
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

			channel.close();
		} catch (Exception var4) {
			logger.error("[{}] ERROR! failed for process message({})", channel, String.format("0x%08x", tMsg.getMessageId()), var4);
		}

	}

	static {
		clientManager = ClientManager.INSTANCE;
		DEFAULT_DATA = "".getBytes();
	}

	private static class ClientManager {
		private static final ClientManager INSTANCE = new ClientManager();
		private int id = 1;
		private final int INIT_SIZE = 4096;
		private final Map<Integer, ClientHandler> clientMap = new HashMap<>(INIT_SIZE);
		private final ReadWriteLock lock;
		private final Set<Integer> clientIds = new LinkedHashSet<>(INIT_SIZE);

		private ClientManager() {
			lock = new ReentrantReadWriteLock();
		}

		/**
		 * 获取id 要么从废旧ID池子 直接拿 要么就再生成 有个隐患 除非同时在线 超过了 Integer.MAX_VALUE 就有几率重复然后客户端错误
		 *
		 * @return 获取链接自增id
		 */
		private int getId() {
			lock.writeLock().lock();
			try {
				int outId = 0;
				if (!clientIds.isEmpty()) {
					for (Integer id : clientIds) {
						outId = id;
						break;
					}
				}

				if (outId > 0) {
					clientIds.remove(outId);
				} else {
					if (id < Integer.MAX_VALUE) {
						outId = ++id;
					} else {
						outId = id = 1;
					}
				}
				return outId;
			} finally {
				lock.writeLock().unlock();
			}
		}

		private void addClient(ClientHandler client) {
			lock.writeLock().lock();
			try {
				clientMap.put(client.getId(), client);
			} finally {
				lock.writeLock().unlock();
			}
		}

		private void removeClient(ClientHandler client) {
			lock.writeLock().lock();
			try {
				removeClient(client.getId());

			} finally {
				lock.writeLock().unlock();
			}
		}

		private void removeClient(int id) {
			lock.writeLock().lock();
			try {
				clientMap.remove(id);
				clientIds.add(id);
			} finally {
				lock.writeLock().unlock();
			}
		}

		private ClientHandler getClient(int id) {
			lock.writeLock().lock();
			try {
				return clientMap.get(id);
			} finally {
				lock.writeLock().unlock();
			}
		}
	}
}
