package net.connect.handle;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.google.protobuf.ByteString;
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
import net.message.Maker;
import net.message.Parser;
import net.message.TCPMaker;
import net.message.TCPMessage;
import net.message.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectHandler extends ChannelInboundHandlerAdapter implements Sender {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectHandler.class);
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
	private EventHandle activeHandle;
	private EventHandle closeEvent;
	private ServerInfo connectServer;
	private ServerInfo localServer;

	private boolean diRetry = false;//断链重试
	private boolean conRetry = false;//链接重试

	public void setDiRetry(boolean diRetry) {
		this.diRetry = diRetry;
	}

	public void setConRetry(boolean conRetry) {
		this.conRetry = conRetry;
	}


	public static Sender getSender(long id) {
		return connectManager.getConnect(id);
	}

	public ConnectHandler(Transfer transfer, Parser parser, Handlers handlers) {
		this(transfer, parser, handlers, TCPMaker.INSTANCE);
	}

	public ConnectHandler(Transfer transfer, Parser parser, Handlers handlers, Maker maker) {
		this.transfer = (t, msg) -> Transfer.DEFAULT();
		MSG_DEFAULT = "".getBytes();
		id = connectManager.getId();
		if (null != transfer) {
			this.transfer = transfer;
		}

		this.parser = parser;
		this.handlers = handlers;
		this.maker = maker;
	}

	public long getId() {
		return id;
	}

	public void setActiveEvent(EventHandle activeEvent) {
		activeHandle = activeEvent;
	}

	public void setCloseEvent(EventHandle closeEvent) {
		this.closeEvent = closeEvent;
	}

	public void setIdleRunner(Consumer<ConnectHandler> runner) {
		idleRunner = runner;
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

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		channel = ctx.channel();
		connectManager.addConnect(this);
		completerGroup = new CompleterGroup(channel.eventLoop());
		if (null != activeHandle) {
			try {
				activeHandle.handle(this);
			} catch (Exception exception) {
				LOGGER.error("[{}] ERROR! failed for activeHandle", ctx.channel());
			}
		}

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		connectManager.removeConnect(getId());
		if (completerGroup != null) {
			completerGroup.destroy();
			completerGroup = null;
		}
		channel = null;
		if (closeEvent != null) {
			try {
				closeEvent.handle(this);
			} catch (Exception exception) {
				LOGGER.error("[{}] ERROR! failed for closeEvent", ctx.channel());
			}
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object event) {
		if (event instanceof IdleStateEvent) {
			LOGGER.debug("[userEventTriggered:{}]", ((IdleStateEvent) event).state());
			if (null != idleRunner) {
				ctx.channel().eventLoop().execute(() -> {
					try {
						idleRunner.accept(this);
					} catch (Exception exception) {
						LOGGER.error("[failed for run idleRunner]", exception);
					}
				});
			}
		} else {
			LOGGER.debug("[userEventTriggered:{}]", event.getClass().getName());
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object o) {
		Message innerMsg;
		Handler handler;
		if (o instanceof TCPMessage) {
			TCPMessage msg = (TCPMessage) o;

			try {
				if (transfer.isTransfer(this, msg)) {
					return;
				}

				if (null != msg.getMessage() && msg.getMessage().length > 0) {
					innerMsg = parser.parser(msg.getMessageId(), msg.getMessage());
				} else {
					innerMsg = parser.parser(msg.getMessageId(), MSG_DEFAULT);
				}


				if (msg.getSequence() != 0 && completerGroup != null) {
					Completer completer = completerGroup.popCompleter(msg.getSequence());
					if (null != completer) {
						completer.msg = innerMsg;
						ctx.channel().eventLoop().execute(completer);
					}
				} else {
					handler = handlers.getHandler(msg.getMessageId());
					if (null != handler) {
						long now = System.currentTimeMillis();
						handler.handler(this, msg.getClientId(), innerMsg, msg.getMapId(), msg.getSequence());
						now = System.currentTimeMillis() - now;
						if (now > 1000L) {
							LOGGER.error("connect handler:{} cost too long :{}ms", handler.getClass().getSimpleName(), now);
						} else {
							LOGGER.debug("connect handler:{} cost:{}ms", handler.getClass().getSimpleName(), now);
						}
					} else {
						LOGGER.error("[{}] ERROR! can not find handler for TCPMessage({})", ctx.channel(), String.format("0x%08x", msg.getMessageId()));
					}
				}

			} catch (Exception exception) {
				LOGGER.error("[{}] ERROR! failed for process TCPMessage({})", ctx.channel(), String.format("0x%08x", msg.getMessageId()), exception);
			}
		} else {
			ctx.fireChannelRead(o);
		}

	}

	@Override
	public void sendMessage(int msgId, Message msg, long sequence) {
		channel.writeAndFlush(maker.wrap(msgId, msg, sequence));
	}

	@Override
	public void sendMessage(int msgId, Message msg) {
		channel.writeAndFlush(maker.wrap(msgId, msg, 0, 0));
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
	public void sendMessage(int msgId, int mapId, Message msg, long sequence) {
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

	/**
	 * 有后续处理的加超时处理
	 */
	public CompletableFuture<com.google.protobuf.Message> sendMessage(int msgId, Message msg, int timeout) {
		long sequence = completerGroup.getSequence();
		Completer completer = new Completer(timeout);
		completerGroup.addCompleter(sequence, completer);
		sendMessage(msgId, 0, msg, sequence);
		return completer;
	}

	static {
		connectManager = ConnectManager.getInstance();
	}

	public void connect(EventLoopGroup eventLoopGroup, SocketAddress socketAddress, ChannelInitializer<SocketChannel> channelInitializer) {
		Bootstrap bootstrap = ((new Bootstrap()).group(eventLoopGroup))
				.channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(channelInitializer);

		try {
			bootstrap.connect(socketAddress).addListener((ChannelFutureListener) cFuture -> {
				InetSocketAddress sad = (InetSocketAddress) socketAddress;
				if (cFuture.isSuccess()) {
					if (diRetry) {
						//链接成功被关闭了也会一直重连
						cFuture.channel().closeFuture().addListener((ChannelFutureListener) f1 -> f1.channel().eventLoop().schedule(() -> connect(f1.channel().eventLoop(), socketAddress, channelInitializer), 3, TimeUnit.SECONDS));
						LOGGER.info("[connect {} is success!!! ]", sad.getHostName() + ":" + sad.getPort());
					}
				} else {
					if (conRetry) {
						//链接失败也会一直重连
						cFuture.channel().eventLoop().schedule(() -> connect(cFuture.channel().eventLoop(), socketAddress, channelInitializer), 3, TimeUnit.SECONDS);
						LOGGER.error("[failed for connect {}!!!]", sad.getHostName() + ":" + sad.getPort());
					}
				}

			}).sync();
		} catch (Exception e) {
			LOGGER.error("{}", e.getMessage());
		}
	}
}
