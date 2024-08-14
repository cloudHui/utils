package net.connect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import net.client.Sender;
import net.client.event.RegisterEvent;
import net.handler.Handler;
import net.handler.Handlers;
import net.message.Maker;
import net.message.Parser;
import net.message.TCPMaker;
import net.message.TCPMessage;
import net.message.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectHandler extends ChannelInboundHandlerAdapter implements Sender {
	private static final Logger logger = LoggerFactory.getLogger(ConnectHandler.class);
	private static final ConnectManager connectManager;
	private final long id;
	private Transfer transfer;
	private final Parser parser;
	private final Handlers handlers;
	private final Maker maker;
	private final byte[] MSG_DEFAULT;
	private Consumer<ConnectHandler> idleRunner;
	private CompleterGroup completerGroup;
	private Channel channel;
	private RegisterEvent registerEvent;
	private long serverId;

	public static Sender getSender(long id) {
		return connectManager.getConnect(id);
	}

	public ConnectHandler(Transfer transfer, Parser parser, Handlers handlers) {
		this(transfer, parser, handlers, TCPMaker.INSTANCE);
	}

	public ConnectHandler(Transfer transfer, Parser parser, Handlers handlers, Maker maker) {
		this.transfer = (t, msg) -> Transfer.DEFAULT();
		this.MSG_DEFAULT = "".getBytes();
		this.id = connectManager.getId();
		if (null != transfer) {
			this.transfer = transfer;
		}

		this.parser = parser;
		this.handlers = handlers;
		this.maker = maker;
	}

	public long getId() {
		return this.id;
	}

	public void setRegisterEvent(RegisterEvent registerEvent) {
		this.registerEvent = registerEvent;
	}

	public void setIdleRunner(Consumer<ConnectHandler> runner) {
		this.idleRunner = runner;
	}

	public void setServerId(long serverId) {
		this.serverId = serverId;
	}

	public long getServerId() {
		return this.serverId;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.channel = ctx.channel();
		connectManager.addConnect(this);
		this.completerGroup = new CompleterGroup(this.channel.eventLoop());
		if (null != this.registerEvent) {
			try {
				this.registerEvent.register(this);
			} catch (Exception var3) {
				logger.error("[{}] ERROR! failed for register", ctx.channel());
			}
		}

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		connectManager.removeConnect(this.getId());
		this.completerGroup.destroy();
		this.completerGroup = null;
		this.channel = null;

	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
		if (IdleStateEvent.class.isAssignableFrom(event.getClass())) {
			IdleStateEvent idle = (IdleStateEvent) event;
			if (IdleState.WRITER_IDLE == idle.state() && null != this.idleRunner) {
				ctx.channel().eventLoop().execute(() -> {
					try {
						this.idleRunner.accept(this);
					} catch (Exception var2) {
						logger.error("failed for run idleRunner", var2);
					}
				});
			}
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object o) {
		Message innerMsg;
		Handler handler;
		if (o instanceof TCPMessage) {
			TCPMessage msg = (TCPMessage) o;

			try {
				if (this.transfer.isTransfer(this, msg)) {
					return;
				}

				if (null != msg.getMessage() && msg.getMessage().length > 0) {
					innerMsg = this.parser.parser(msg.getMessageId(), msg.getMessage());
				} else {
					innerMsg = this.parser.parser(msg.getMessageId(), this.MSG_DEFAULT);
				}

				handler = this.handlers.getHandler(msg.getMessageId());
				if (null != handler) {
					long now = System.currentTimeMillis();
					handler.handler(this, (long) msg.getRoleId(), innerMsg, msg.getMapId());
					now = System.currentTimeMillis() - now;
					if (now > 1000L) {
						logger.error("connect handler:{} cost too long :{}ms", handler.getClass().getSimpleName(), now);
					} else {
						logger.warn("connect handler:{} cost:{}ms", handler.getClass().getSimpleName(), now);
					}
				} else {
					logger.error("[{}] ERROR! can not find handler for TCPMessage({})", ctx.channel(), String.format("0x%08x", msg.getMessageId()));
				}
			} catch (Exception var6) {
				logger.error("[{}] ERROR! failed for process TCPMessage({})", new Object[]{ctx.channel(), String.format("0x%08x", msg.getMessageId()), var6});
			}
		} else {
			ctx.fireChannelRead(o);
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
	public void sendMessage(int sequence, int msgId, Message msg, Map<Long, String> attachments) {
		this.channel.writeAndFlush(this.maker.wrap(sequence, msgId, msg, attachments));
	}

	@Override
	public void sendMessage(TCPMessage msg) {
		this.channel.writeAndFlush(msg);
	}

	@Override
	public void sendMessage(int roleId, int msgId, int mapId, int resultId, Message msg) {
		this.channel.writeAndFlush(this.maker.wrap(roleId, msgId, mapId, resultId, msg));
	}

	public CompletableFuture sendMessage(int msgId, Message msg, Map<Long, String> attachments, long timeout) {
		int sequence = this.completerGroup.getSequence();
		Completer completer = new Completer(timeout);
		this.completerGroup.addCompleter(sequence, completer);
		this.sendMessage(sequence, msgId, msg, attachments);
		return completer;
	}

	static {
		connectManager = ConnectManager.INSTANCE;
	}

	private static class CompleterGroup implements Runnable, Comparable<CompleterGroup> {
		private static final Throwable TIMEOUT = new RuntimeException("timeout");
		private Map<Long, Completer> completerMap = new ConcurrentHashMap(128);
		private EventLoop executors;
		private static final AtomicInteger sequence = new AtomicInteger(0);
		private static final Set<Runnable> runners = new ConcurrentSkipListSet();
		private static final Thread checker = new Thread(() -> {
			long waitTime;

			while (true) {
				while (true) {
					waitTime = System.currentTimeMillis();
					boolean var18 = false;

					try {
						var18 = true;
						runners.forEach(Runnable::run);
						var18 = false;
						break;
					} catch (Exception var25) {
						ConnectHandler.logger.error("", var25);
						var18 = false;
					} finally {
						if (var18) {
							waitTime = 1000L - (System.currentTimeMillis() - waitTime);
							if (waitTime > 10L) {
								synchronized (runners) {
									try {
										runners.wait(waitTime);
									} catch (Exception var19) {
										ConnectHandler.logger.error("", var19);
									}
								}
							}

						}
					}

					waitTime = 1000L - (System.currentTimeMillis() - waitTime);
					if (waitTime > 10L) {
						synchronized (runners) {
							try {
								runners.wait(waitTime);
							} catch (Exception var23) {
								ConnectHandler.logger.error("", var23);
							}
						}
					}
				}

				waitTime = 1000L - (System.currentTimeMillis() - waitTime);
				if (waitTime > 10L) {
					synchronized (runners) {
						try {
							runners.wait(waitTime);
						} catch (Exception var21) {
							ConnectHandler.logger.error("", var21);
						}
					}
				}
			}
		});

		@Override
		public int compareTo(CompleterGroup o) {
			return this.equals(o) ? 0 : 1;
		}

		public CompleterGroup(EventLoop eventExecutors) {
			this.executors = eventExecutors;
			runners.add(this);
		}

		public int getSequence() {
			return sequence.incrementAndGet();
		}

		public void addCompleter(long sequence, Completer completer) {
			this.completerMap.put(sequence, completer);
		}

		public Completer popCompleter(long sequence) {
			return this.completerMap.remove(sequence);
		}

		public void destroy() {
			runners.remove(this);

			try {
				Thread.sleep(3000L);
			} catch (Exception var6) {
			}

			Completer completer;

			while (!this.completerMap.isEmpty()) {
				Throwable ex = new RuntimeException("Unknown exception occurred！");
				Set<Long> keys = this.completerMap.keySet();

				for (Long id : keys) {
					completer = this.completerMap.remove(id);
					if (null != completer) {
						completer.ex = ex;
						this.executors.execute(completer);
					}
				}
			}

			this.executors = null;
		}

		@Override
		public void run() {
			try {
				Set<Long> seq = new HashSet<>();
				long nowTime = System.currentTimeMillis();
				this.completerMap.forEach((k, o) -> {
					if (o.isTimeout(nowTime)) {
						seq.add(k);
					}

				});
				if (!seq.isEmpty()) {
					Completer completer;

					for (Long id : seq) {
						completer = this.completerMap.remove(id);
						if (null != completer) {
							completer.ex = TIMEOUT;
							this.executors.execute(completer);
						}
					}
				}
			} catch (Exception ignored) {
			}

		}

		static {
			checker.start();
		}
	}

	private static class Completer<T> extends CompletableFuture<T> implements Runnable {
		private long timeout;
		public Throwable ex;
		public T msg;

		public Completer(long timeout) {
			this.timeout = timeout * 1000L + System.currentTimeMillis();
		}

		public boolean isTimeout(long time) {
			return time >= this.timeout;
		}

		@Override
		public void run() {
			try {
				if (null != this.ex) {
					this.completeExceptionally(this.ex);
				} else {
					this.complete(this.msg);
				}
			} catch (Exception var2) {
				ConnectHandler.logger.error("", var2);
			}

		}
	}

	private static class ConnectManager {
		private static final ConnectManager INSTANCE = new ConnectManager();
		private final AtomicLong ID = new AtomicLong(0L);
		private final Map<Long, ConnectHandler> connectMap = new HashMap(64);
		private final ReadWriteLock lock = new ReentrantReadWriteLock(false);

		private ConnectManager() {
		}

		public long getId() {
			return this.ID.incrementAndGet();
		}

		private void addConnect(ConnectHandler connect) {
			this.lock.writeLock().lock();

			try {
				this.connectMap.put(connect.getId(), connect);
			} finally {
				this.lock.writeLock().unlock();
			}

		}

		private void removeConnect(long id) {
			this.lock.writeLock().lock();

			try {
				this.connectMap.remove(id);
			} finally {
				this.lock.writeLock().unlock();
			}

		}

		private ConnectHandler getConnect(long id) {
			this.lock.readLock().lock();

			ConnectHandler var3;
			try {
				var3 = this.connectMap.get(id);
			} finally {
				this.lock.readLock().unlock();
			}

			return var3;
		}
	}
}
