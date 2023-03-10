package http;

import java.net.InetSocketAddress;

import http.handler.Handler;
import http.handler.Maker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HttpDecoder extends ChannelInboundHandlerAdapter implements Linker {
	private final static Logger LOGGER = LoggerFactory.getLogger(HttpDecoder.class);
	public final static String WEB_SOCKET = "websocket";
	private Channel channel;
	private String ip;

	private long lastMsgStamp;

	private Maker maker;

	public HttpDecoder() {
		this(null);
	}

	public HttpDecoder(Maker maker) {
		this.maker = maker;
	}

	public HttpDecoder setMaker(Maker maker) {
		this.maker = maker;
		return this;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		channel = ctx.channel();
		ip = ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object o) throws Exception {
		try {
			long now = System.currentTimeMillis();
			if (lastMsgStamp != 0 && now - lastMsgStamp < 2000) {
				return;
			}
			lastMsgStamp = now;
			if (o instanceof FullHttpRequest) {
				FullHttpRequest request = (FullHttpRequest) o;
				try {
					String path = request.getUri();
					if (null != path && path.startsWith("/")) {
						path = path.substring(1);
					}

					String[] data = path.split("\\?");
					if (data.length > 0) {
						if (HttpMethod.POST.equals(request.getMethod())) {
							Handler handler = getHandler(data[0]);
							if (null != handler) {
								HttpMethod method = request.getMethod();
								if (!handler.handler(this, data[0], method.name(), handler.parser(getBody(request)))) {
									//if (!handler.handler(this, data[0], method.name(), handler.parser(data.length > 1? data[1]: getBody(request)))) {
									LOGGER.info("[{}] process message return false", ctx.channel());
									ctx.close();
								}
							} else {
								LOGGER.info("[{}] can not find handler(POST) for path({})", ctx.channel(), path);
							}
						} else if (HttpMethod.GET.equals(request.getMethod())) {
							Handler handler = getHandler(data[0]);
							if (null != handler) {
								HttpMethod method = request.getMethod();
								if (!handler.handler(this, data[0], method.name(), handler.parser(data.length > 1 ? data[1] : null))) {
									LOGGER.info("[{}] process message return false", ctx.channel());
									ctx.close();
								}
							} else {
								LOGGER.info("[{}] can not find handler(GET) for path({} {})", ctx.channel(), data[0], path);
							}
						} else {
							ctx.close();
							LOGGER.info("[{}] unsupported method({} path:{})", ctx.channel(), request.getMethod().name(), path);
						}
					} else {
						ctx.close();
						LOGGER.info("[{}] unsupported({} path:{})", ctx.channel(), request.getMethod().name(), path);
					}
				} catch (Throwable e) {
					LOGGER.error("", e);
					ctx.close();
				}
			} else if (o instanceof WebSocketFrame) {
				WebSocketFrame frame = (WebSocketFrame) o;
				try {
					ByteBuf buf = frame.content();
					if (buf.readableBytes() > 0) {
						byte[] bytes = new byte[buf.readableBytes()];
						buf.readBytes(bytes);
						Handler handler = getHandler(WEB_SOCKET);
						if (null != handler) {
							if (!handler.handler(this, null, WEB_SOCKET, handler.parser(new String(bytes, CharsetUtil.UTF_8)))) {
								LOGGER.info("[{}] process message return false", ctx.channel());
								ctx.close();
							}
						} else {
							LOGGER.info("[{}] can not find handler for web socket", ctx.channel());
						}
					}
				} catch (Throwable e) {
					LOGGER.error("", e);
					ctx.close();
				} finally {
					ReferenceCountUtil.release(frame);
				}
			} else {
				ctx.fireChannelRead(o);
			}
		} catch (Throwable t) {
			LOGGER.error("[{}] ERROR! failed for process message", ctx.channel(), t);
		}
	}

	private String getBody(FullHttpRequest request) {
		ByteBuf d = null;
		try {
			d = request.content();
			return d.toString(CharsetUtil.UTF_8);
		} finally {
			if (null != d) {
				d.release();
			}
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) throws Exception {
		if (null != channel && channel.isActive()) {
			channel.close();
		}
	}

	@Override
	public String remoteIp() {
		return ip;
	}

	@Override
	public <T> void sendMessage(int msgId, T msg) {
		try {
			sendMsg(maker.wrap(msgId, msg));
		} catch (Exception e) {
			LOGGER.error("[{}] failed for send message(id:{} msg:{})", channel, msgId, msg, e);
		}
	}

	@Override
	public <T> void sendMessage(T msg) {
		try {
			sendMsg(maker.wrap(msg));
		} catch (Exception e) {
			LOGGER.error("[{}] failed for send message({})", channel, msg.toString(), e);
		}
	}

	@Override
	public void sendMessage(String msg) {
		try {
			sendMsg(maker.wrap(msg));
		} catch (Exception e) {
			LOGGER.error("[{}] failed for send message({})", channel, msg, e);
		}
	}

	private <T> void sendMsg(T t) {
		channel.writeAndFlush(t);
	}

	public abstract Handler getHandler(String path);
}
