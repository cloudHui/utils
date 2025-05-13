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
			if (lastMsgStamp != 0 && now - lastMsgStamp < 1000L) {
				return;
			}
			lastMsgStamp = now;
			if (o instanceof FullHttpRequest) {
				dealFullHttpMsg(o);
			} else if (o instanceof WebSocketFrame) {
				dealWebsocketMsg(o);
			} else {
				ctx.fireChannelRead(o);
			}
		} catch (Throwable t) {
			LOGGER.error("[{}] ERROR! failed for process message", ctx.channel(), t);
		}
	}

	/**
	 * 处理http消息
	 */
	private void dealFullHttpMsg(Object o) {
		FullHttpRequest request = (FullHttpRequest) o;
		try {
			String path = request.uri();
			if (null != path && path.startsWith("/")) {
				path = path.substring(1);
			}

			if (path == null) {
				channel.close();
				LOGGER.info("[{}] unsupported({} path:null)", channel, request.content().toString(CharsetUtil.UTF_8));
				return;
			}
			String[] data = path.split("\\?");
			if (data.length <= 0) {
				channel.close();
				LOGGER.info("[{}] unsupported({} data:null)", channel, path);
				return;
			}
			if (HttpMethod.POST.equals(request.method())) {
				httpPost(data, request, path);
			} else if (HttpMethod.GET.equals(request.method())) {
				httpGet(data, path);
			} else {
				channel.close();
				LOGGER.info("[{}] unsupported method({} path:{})", channel, request.method().name(), path);
			}
		} catch (Throwable e) {
			LOGGER.error("", e);
			channel.close();
		}
	}

	/**
	 * http get
	 */
	private void httpGet(String[] data, String path) {
		Handler handler = getHandler(data[0]);
		if (null == handler) {
			LOGGER.info("[{}] can not find handler httpGet for {}  path:{}", channel, data[0], path);
			channel.close();
			return;
		}
		long now = System.currentTimeMillis();


		boolean keepChannel = handler.handler(this, handler.parser(data.length > 1 ? data[1] : null), ip);

		now = System.currentTimeMillis() - now;
		if (now > 1000L) {
			LOGGER.error("httpGet handler:{} cost too long:{}ms", handler.getClass().getSimpleName(), now);
		} else {
			LOGGER.debug("httpGet handler:{} cost:{}ms", handler.getClass().getSimpleName(), now);
		}

		if (!keepChannel) {
			LOGGER.info("[{}] process message return false", channel);
			channel.close();
		}
	}

	/**
	 * http post
	 */
	private void httpPost(String[] data, FullHttpRequest request, String path) {
		Handler handler = getHandler(data[0]);
		if (null == handler) {
			LOGGER.info("[{}] can not find handler httpPost for path:{}", channel, path);
			channel.close();
			return;
		}

		long now = System.currentTimeMillis();
		boolean keepChannel = handler.handler(this, handler.parser(getBody(request)), ip);

		now = System.currentTimeMillis() - now;
		if (now > 1000L) {
			LOGGER.error("httpPost handler:{} cost too long:{}ms", handler.getClass().getSimpleName(), now);
		} else {
			LOGGER.debug("httpPost handler:{} cost:{}ms", handler.getClass().getSimpleName(), now);
		}

		if (!keepChannel) {
			LOGGER.info("[{}] process message return false", channel);
			channel.close();
		}
	}

	/**
	 * 处理 websocket消息
	 */
	private void dealWebsocketMsg(Object o) {
		WebSocketFrame frame = (WebSocketFrame) o;
		try {
			ByteBuf buf = frame.content();
			if (buf.readableBytes() > 0) {
				byte[] bytes = new byte[buf.readableBytes()];
				buf.readBytes(bytes);
				Handler handler = getHandler(WEB_SOCKET);
				if (null != handler) {
					LOGGER.info("remote {}", channel.remoteAddress().toString().replace("/", "").split(":")[0]);
					if (!handler.handler(this, handler.parser(new String(bytes, CharsetUtil.UTF_8)), ip)) {
						LOGGER.info("[{}] process message return false", channel);
						channel.close();
					}
				} else {
					LOGGER.info("[{}] can not find handler for web socket", channel);
				}
			}
		} catch (Throwable e) {
			LOGGER.error("", e);
			channel.close();
		} finally {
			ReferenceCountUtil.release(frame);
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
		LOGGER.error("[{}] close", ctx.channel());
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
