package net.connect;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.google.protobuf.Message;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import net.client.event.EventHandle;
import net.codec.TCPMessageDecoder;
import net.codec.TCPMessageEncoder;
import net.connect.handle.ConnectHandler;
import net.handler.Handlers;
import net.message.Parser;
import net.message.TCPMaker;
import net.message.Transfer;

@Sharable
public class TCPConnect extends ConnectHandler {
	private final EventLoopGroup eventLoopGroup;
	private final SocketAddress socketAddress;

	/**
	 * 是否有回调消息
	 */
	private CallParam callParam = null;

	/**
	 * Tcp链接器
	 *
	 * @param transfer 转发消息接口
	 * @param parser   消息转化接口
	 * @param handlers 消息处理接口
	 * @param active   链接活跃处理器接口
	 * @param close    链接关闭处理器接口
	 */
	public TCPConnect(EventLoopGroup eventLoopGroup, SocketAddress socketAddress, Transfer transfer, Parser parser, Handlers handlers, EventHandle active, EventHandle close) {
		super(transfer, parser, handlers, TCPMaker.INSTANCE);
		this.eventLoopGroup = eventLoopGroup;
		this.socketAddress = socketAddress;
		setActiveEvent(active);
		setCloseEvent(close);
	}

	public String getIP() {
		return ((InetSocketAddress) socketAddress).getAddress().getHostAddress();
	}

	public int getPort() {
		return ((InetSocketAddress) socketAddress).getPort();
	}

	public TCPConnect connect() {
		connect(eventLoopGroup, socketAddress, new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new IdleStateHandler(0, 60, 0));
				p.addLast(new TCPMessageEncoder());
				p.addLast(new TCPMessageDecoder());
				p.addLast(TCPConnect.this);
			}
		});
		return this;
	}

	@Override
	public String toString() {
		return "TCPConnect{" + "connectServer=" + getConnectServer() + ", localServer=" + getLocalServer() + '}';
	}

	public CallParam getCallParam() {
		return callParam;
	}

	/**
	 * 设置注册成功回调
	 */
	public void setCallParam(CallParam callParam) {
		this.callParam = callParam;
	}

	/**
	 * 回调参数
	 */
	public static class CallParam {
		public int messageId;

		public Message message;

		public Parser parser;

		public RegisterCallback callback = null;

		public CallParam(int messageId, Message message) {
			this.messageId = messageId;
			this.message = message;
		}

		public CallParam(int messageId, Message message, RegisterCallback callback, Parser parser) {
			this.messageId = messageId;
			this.message = message;
			this.callback = callback;
			this.parser = parser;
		}
	}

	/**
	 * 超时消息发送回调
	 */
	@FunctionalInterface
	public interface RegisterCallback {
		void handle(int msgId, Message message, ConnectHandler serverClient, Parser parser);
	}
}
