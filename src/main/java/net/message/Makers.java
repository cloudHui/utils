package net.message;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import net.proto.CommonProto.KStrVPair;
import net.proto.SysProto.SysMessage;
import net.proto.SysProto.SysMessage.Builder;

import java.util.Map;
import java.util.Map.Entry;

public class Makers {
	private static Maker<SysMessage> maker = new Maker<SysMessage>() {
		public String version() {
			return "1.0";
		}

		public SysMessage wrap(Integer msgId, Message msg, Map<Long, String> attachments) {
			Builder sysMessage = SysMessage.newBuilder().setVersion(ByteString.copyFromUtf8(this.version())).setMsgId(msgId);
			if (null != msg) {
				sysMessage.setInnerMsg(msg.toByteString());
			}

			if (null != attachments && !attachments.isEmpty()) {

				for (Entry<Long, String> longStringEntry : attachments.entrySet()) {
					KStrVPair kv = KStrVPair.newBuilder().setKey((Long) (longStringEntry).getKey()).
							setValue(null == (longStringEntry).getValue()
									? ByteString.EMPTY : ByteString.copyFromUtf8((String) (longStringEntry).getValue())).build();
					sysMessage.addExtends(kv);
				}
			}

			return sysMessage.build();
		}

		public SysMessage wrap(Long sequence, Integer msgId, Message msg, Map<Long, String> attachments) {
			Builder sysMessage = SysMessage.newBuilder().setVersion(ByteString.copyFromUtf8(this.version()))
					.setSequence(sequence).setMsgId(msgId);
			if (null != msg) {
				sysMessage.setInnerMsg(msg.toByteString());
			}

			if (null != attachments && !attachments.isEmpty()) {

				for (Entry<Long, String> longStringEntry : attachments.entrySet()) {
					KStrVPair kv = KStrVPair.newBuilder().setKey((longStringEntry).getKey()).
							setValue(null == (longStringEntry).getValue() ? ByteString.EMPTY
									: ByteString.copyFromUtf8((longStringEntry).getValue())).build();
					sysMessage.addExtends(kv);
				}
			}

			return sysMessage.build();
		}

		public SysMessage wrap(Integer msgId, ByteString msg, Map<Long, String> attachments) {
			Builder sysMessage = SysMessage.newBuilder().setVersion(ByteString.copyFromUtf8(this.version())).
					setMsgId(msgId);
			if (null != msg) {
				sysMessage.setInnerMsg(msg);
			}

			if (null != attachments && !attachments.isEmpty()) {

				for (Entry<Long, String> longStringEntry : attachments.entrySet()) {
					KStrVPair kv = KStrVPair.newBuilder().setKey((Long) (longStringEntry).getKey()).
							setValue(null == (longStringEntry).getValue() ? ByteString.EMPTY :
									ByteString.copyFromUtf8((longStringEntry).getValue())).build();
					sysMessage.addExtends(kv);
				}
			}

			return sysMessage.build();
		}
	};

	public Makers() {
	}

	public static void setMaker(Maker maker) {
		Makers.maker = maker;
	}

	public static Maker getMaker() {
		return maker;
	}
}
