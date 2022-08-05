package net.proto;

import com.google.protobuf.AbstractParser;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.GeneratedMessage.FieldAccessorTable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.RepeatedFieldBuilder;
import com.google.protobuf.UnknownFieldSet;
import net.proto.CommonProto.KStrVPair;
import net.proto.CommonProto.KStrVPairOrBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SysProto {
	private static Descriptor internal_static_proto_SysMessage_descriptor;
	private static FieldAccessorTable internal_static_proto_SysMessage_fieldAccessorTable;
	private static FileDescriptor descriptor;

	private SysProto() {
	}

	public static void registerAllExtensions(ExtensionRegistry registry) {
	}

	public static FileDescriptor getDescriptor() {
		return descriptor;
	}

	static {
		String[] descriptorData = new String[]{"\n\tsys.proto\u0012\u0005proto\u001a\fcommon.proto\"s\n\nSysMessage\u0012\u000f\n\u0007version\u0018\u0001 \u0001(\f\u0012\u0010\n\bsequence\u0018\u0002 \u0001(\u0003\u0012\r\n\u0005msgId\u0018\u0003 \u0002(\u0005\u0012\u0010\n\binnerMsg\u0018\u0004 \u0001(\f\u0012!\n\u0007extends\u0018\u0005 \u0003(\u000b2\u0010.proto.KStrVPairB\nB\bSysProto"};
		InternalDescriptorAssigner assigner = new InternalDescriptorAssigner() {
			public ExtensionRegistry assignDescriptors(FileDescriptor root) {
				SysProto.descriptor = root;
				SysProto.internal_static_proto_SysMessage_descriptor = (Descriptor) SysProto.getDescriptor().getMessageTypes().get(0);
				SysProto.internal_static_proto_SysMessage_fieldAccessorTable = new FieldAccessorTable(SysProto.internal_static_proto_SysMessage_descriptor, new String[]{"Version", "Sequence", "MsgId", "InnerMsg", "Extends"});
				return null;
			}
		};
		FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new FileDescriptor[]{CommonProto.getDescriptor()}, assigner);
	}

	public static final class SysMessage extends GeneratedMessage implements SysMessageOrBuilder {
		private static final SysMessage defaultInstance = new SysMessage(true);
		private final UnknownFieldSet unknownFields;
		public static Parser<SysMessage> PARSER = new AbstractParser<SysMessage>() {
			public SysMessage parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
				return new SysMessage(input, extensionRegistry);
			}
		};
		private int bitField0_;
		public static final int VERSION_FIELD_NUMBER = 1;
		private ByteString version_;
		public static final int SEQUENCE_FIELD_NUMBER = 2;
		private long sequence_;
		public static final int MSGID_FIELD_NUMBER = 3;
		private int msgId_;
		public static final int INNERMSG_FIELD_NUMBER = 4;
		private ByteString innerMsg_;
		public static final int EXTENDS_FIELD_NUMBER = 5;
		private List<KStrVPair> extends_;
		private byte memoizedIsInitialized;
		private int memoizedSerializedSize;
		private static final long serialVersionUID = 0L;

		private SysMessage(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
			super(builder);
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.unknownFields = builder.getUnknownFields();
		}

		private SysMessage(boolean noInit) {
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.unknownFields = UnknownFieldSet.getDefaultInstance();
		}

		public static SysMessage getDefaultInstance() {
			return defaultInstance;
		}

		public SysMessage getDefaultInstanceForType() {
			return defaultInstance;
		}

		public final UnknownFieldSet getUnknownFields() {
			return this.unknownFields;
		}

		private SysMessage(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.initFields();
			int mutable_bitField0_ = 0;
			com.google.protobuf.UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();

			try {
				boolean done = false;

				while (!done) {
					int tag = input.readTag();
					switch (tag) {
						case 0:
							done = true;
							break;
						case 10:
							this.bitField0_ |= 1;
							this.version_ = input.readBytes();
							break;
						case 16:
							this.bitField0_ |= 2;
							this.sequence_ = input.readInt64();
							break;
						case 24:
							this.bitField0_ |= 4;
							this.msgId_ = input.readInt32();
							break;
						case 34:
							this.bitField0_ |= 8;
							this.innerMsg_ = input.readBytes();
							break;
						case 42:
							if ((mutable_bitField0_ & 16) != 16) {
								this.extends_ = new ArrayList();
								mutable_bitField0_ |= 16;
							}

							this.extends_.add(input.readMessage(KStrVPair.PARSER, extensionRegistry));
							break;
						default:
							if (!this.parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
								done = true;
							}
					}
				}
			} catch (InvalidProtocolBufferException var11) {
				throw var11.setUnfinishedMessage(this);
			} catch (IOException var12) {
				throw (new InvalidProtocolBufferException(var12.getMessage())).setUnfinishedMessage(this);
			} finally {
				if ((mutable_bitField0_ & 16) == 16) {
					this.extends_ = Collections.unmodifiableList(this.extends_);
				}

				this.unknownFields = unknownFields.build();
				this.makeExtensionsImmutable();
			}

		}

		public static final Descriptor getDescriptor() {
			return SysProto.internal_static_proto_SysMessage_descriptor;
		}

		protected FieldAccessorTable internalGetFieldAccessorTable() {
			return SysProto.internal_static_proto_SysMessage_fieldAccessorTable.ensureFieldAccessorsInitialized(SysMessage.class, Builder.class);
		}

		public Parser<SysMessage> getParserForType() {
			return PARSER;
		}

		public boolean hasVersion() {
			return (this.bitField0_ & 1) == 1;
		}

		public ByteString getVersion() {
			return this.version_;
		}

		public boolean hasSequence() {
			return (this.bitField0_ & 2) == 2;
		}

		public long getSequence() {
			return this.sequence_;
		}

		public boolean hasMsgId() {
			return (this.bitField0_ & 4) == 4;
		}

		public int getMsgId() {
			return this.msgId_;
		}

		public boolean hasInnerMsg() {
			return (this.bitField0_ & 8) == 8;
		}

		public ByteString getInnerMsg() {
			return this.innerMsg_;
		}

		public List<KStrVPair> getExtendsList() {
			return this.extends_;
		}

		public List<? extends KStrVPairOrBuilder> getExtendsOrBuilderList() {
			return this.extends_;
		}

		public int getExtendsCount() {
			return this.extends_.size();
		}

		public KStrVPair getExtends(int index) {
			return (KStrVPair) this.extends_.get(index);
		}

		public KStrVPairOrBuilder getExtendsOrBuilder(int index) {
			return (KStrVPairOrBuilder) this.extends_.get(index);
		}

		private void initFields() {
			this.version_ = ByteString.EMPTY;
			this.sequence_ = 0L;
			this.msgId_ = 0;
			this.innerMsg_ = ByteString.EMPTY;
			this.extends_ = Collections.emptyList();
		}

		public final boolean isInitialized() {
			byte isInitialized = this.memoizedIsInitialized;
			if (isInitialized != -1) {
				return isInitialized == 1;
			} else if (!this.hasMsgId()) {
				this.memoizedIsInitialized = 0;
				return false;
			} else {
				this.memoizedIsInitialized = 1;
				return true;
			}
		}

		public void writeTo(CodedOutputStream output) throws IOException {
			this.getSerializedSize();
			if ((this.bitField0_ & 1) == 1) {
				output.writeBytes(1, this.version_);
			}

			if ((this.bitField0_ & 2) == 2) {
				output.writeInt64(2, this.sequence_);
			}

			if ((this.bitField0_ & 4) == 4) {
				output.writeInt32(3, this.msgId_);
			}

			if ((this.bitField0_ & 8) == 8) {
				output.writeBytes(4, this.innerMsg_);
			}

			for (int i = 0; i < this.extends_.size(); ++i) {
				output.writeMessage(5, (MessageLite) this.extends_.get(i));
			}

			this.getUnknownFields().writeTo(output);
		}

		public int getSerializedSize() {
			int size = this.memoizedSerializedSize;
			if (size != -1) {
				return size;
			} else {
				size = 0;
				if ((this.bitField0_ & 1) == 1) {
					size += CodedOutputStream.computeBytesSize(1, this.version_);
				}

				if ((this.bitField0_ & 2) == 2) {
					size += CodedOutputStream.computeInt64Size(2, this.sequence_);
				}

				if ((this.bitField0_ & 4) == 4) {
					size += CodedOutputStream.computeInt32Size(3, this.msgId_);
				}

				if ((this.bitField0_ & 8) == 8) {
					size += CodedOutputStream.computeBytesSize(4, this.innerMsg_);
				}

				for (int i = 0; i < this.extends_.size(); ++i) {
					size += CodedOutputStream.computeMessageSize(5, (MessageLite) this.extends_.get(i));
				}

				size += this.getUnknownFields().getSerializedSize();
				this.memoizedSerializedSize = size;
				return size;
			}
		}

		protected Object writeReplace() throws ObjectStreamException {
			return super.writeReplace();
		}

		public static SysMessage parseFrom(ByteString data) throws InvalidProtocolBufferException {
			return (SysMessage) PARSER.parseFrom(data);
		}

		public static SysMessage parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			return (SysMessage) PARSER.parseFrom(data, extensionRegistry);
		}

		public static SysMessage parseFrom(byte[] data) throws InvalidProtocolBufferException {
			return (SysMessage) PARSER.parseFrom(data);
		}

		public static SysMessage parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			return (SysMessage) PARSER.parseFrom(data, extensionRegistry);
		}

		public static SysMessage parseFrom(InputStream input) throws IOException {
			return (SysMessage) PARSER.parseFrom(input);
		}

		public static SysMessage parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (SysMessage) PARSER.parseFrom(input, extensionRegistry);
		}

		public static SysMessage parseDelimitedFrom(InputStream input) throws IOException {
			return (SysMessage) PARSER.parseDelimitedFrom(input);
		}

		public static SysMessage parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (SysMessage) PARSER.parseDelimitedFrom(input, extensionRegistry);
		}

		public static SysMessage parseFrom(CodedInputStream input) throws IOException {
			return (SysMessage) PARSER.parseFrom(input);
		}

		public static SysMessage parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (SysMessage) PARSER.parseFrom(input, extensionRegistry);
		}

		public static Builder newBuilder() {
			return Builder.create();
		}

		public Builder newBuilderForType() {
			return newBuilder();
		}

		public static Builder newBuilder(SysMessage prototype) {
			return newBuilder().mergeFrom(prototype);
		}

		public Builder toBuilder() {
			return newBuilder(this);
		}

		protected Builder newBuilderForType(BuilderParent parent) {
			Builder builder = new Builder(parent);
			return builder;
		}

		static {
			defaultInstance.initFields();
		}

		public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements SysMessageOrBuilder {
			private int bitField0_;
			private ByteString version_;
			private long sequence_;
			private int msgId_;
			private ByteString innerMsg_;
			private List<KStrVPair> extends_;
			private RepeatedFieldBuilder<KStrVPair, net.proto.CommonProto.KStrVPair.Builder, KStrVPairOrBuilder> extendsBuilder_;

			public static final Descriptor getDescriptor() {
				return SysProto.internal_static_proto_SysMessage_descriptor;
			}

			protected FieldAccessorTable internalGetFieldAccessorTable() {
				return SysProto.internal_static_proto_SysMessage_fieldAccessorTable.ensureFieldAccessorsInitialized(SysMessage.class, Builder.class);
			}

			private Builder() {
				this.version_ = ByteString.EMPTY;
				this.innerMsg_ = ByteString.EMPTY;
				this.extends_ = Collections.emptyList();
				this.maybeForceBuilderInitialization();
			}

			private Builder(BuilderParent parent) {
				super(parent);
				this.version_ = ByteString.EMPTY;
				this.innerMsg_ = ByteString.EMPTY;
				this.extends_ = Collections.emptyList();
				this.maybeForceBuilderInitialization();
			}

			private void maybeForceBuilderInitialization() {
				if (SysMessage.alwaysUseFieldBuilders) {
					this.getExtendsFieldBuilder();
				}

			}

			private static Builder create() {
				return new Builder();
			}

			public Builder clear() {
				super.clear();
				this.version_ = ByteString.EMPTY;
				this.bitField0_ &= -2;
				this.sequence_ = 0L;
				this.bitField0_ &= -3;
				this.msgId_ = 0;
				this.bitField0_ &= -5;
				this.innerMsg_ = ByteString.EMPTY;
				this.bitField0_ &= -9;
				if (this.extendsBuilder_ == null) {
					this.extends_ = Collections.emptyList();
					this.bitField0_ &= -17;
				} else {
					this.extendsBuilder_.clear();
				}

				return this;
			}

			public Builder clone() {
				return create().mergeFrom(this.buildPartial());
			}

			public Descriptor getDescriptorForType() {
				return SysProto.internal_static_proto_SysMessage_descriptor;
			}

			public SysMessage getDefaultInstanceForType() {
				return SysMessage.getDefaultInstance();
			}

			public SysMessage build() {
				SysMessage result = this.buildPartial();
				if (!result.isInitialized()) {
					throw newUninitializedMessageException(result);
				} else {
					return result;
				}
			}

			public SysMessage buildPartial() {
				SysMessage result = new SysMessage(this);
				int from_bitField0_ = this.bitField0_;
				int to_bitField0_ = 0;
				if ((from_bitField0_ & 1) == 1) {
					to_bitField0_ |= 1;
				}

				result.version_ = this.version_;
				if ((from_bitField0_ & 2) == 2) {
					to_bitField0_ |= 2;
				}

				result.sequence_ = this.sequence_;
				if ((from_bitField0_ & 4) == 4) {
					to_bitField0_ |= 4;
				}

				result.msgId_ = this.msgId_;
				if ((from_bitField0_ & 8) == 8) {
					to_bitField0_ |= 8;
				}

				result.innerMsg_ = this.innerMsg_;
				if (this.extendsBuilder_ == null) {
					if ((this.bitField0_ & 16) == 16) {
						this.extends_ = Collections.unmodifiableList(this.extends_);
						this.bitField0_ &= -17;
					}

					result.extends_ = this.extends_;
				} else {
					result.extends_ = this.extendsBuilder_.build();
				}

				result.bitField0_ = to_bitField0_;
				this.onBuilt();
				return result;
			}

			public Builder mergeFrom(Message other) {
				if (other instanceof SysMessage) {
					return this.mergeFrom((SysMessage) other);
				} else {
					super.mergeFrom(other);
					return this;
				}
			}

			public Builder mergeFrom(SysMessage other) {
				if (other == SysMessage.getDefaultInstance()) {
					return this;
				} else {
					if (other.hasVersion()) {
						this.setVersion(other.getVersion());
					}

					if (other.hasSequence()) {
						this.setSequence(other.getSequence());
					}

					if (other.hasMsgId()) {
						this.setMsgId(other.getMsgId());
					}

					if (other.hasInnerMsg()) {
						this.setInnerMsg(other.getInnerMsg());
					}

					if (this.extendsBuilder_ == null) {
						if (!other.extends_.isEmpty()) {
							if (this.extends_.isEmpty()) {
								this.extends_ = other.extends_;
								this.bitField0_ &= -17;
							} else {
								this.ensureExtendsIsMutable();
								this.extends_.addAll(other.extends_);
							}

							this.onChanged();
						}
					} else if (!other.extends_.isEmpty()) {
						if (this.extendsBuilder_.isEmpty()) {
							this.extendsBuilder_.dispose();
							this.extendsBuilder_ = null;
							this.extends_ = other.extends_;
							this.bitField0_ &= -17;
							this.extendsBuilder_ = SysMessage.alwaysUseFieldBuilders ? this.getExtendsFieldBuilder() : null;
						} else {
							this.extendsBuilder_.addAllMessages(other.extends_);
						}
					}

					this.mergeUnknownFields(other.getUnknownFields());
					return this;
				}
			}

			public final boolean isInitialized() {
				return this.hasMsgId();
			}

			public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
				SysMessage parsedMessage = null;

				try {
					parsedMessage = (SysMessage) SysMessage.PARSER.parsePartialFrom(input, extensionRegistry);
				} catch (InvalidProtocolBufferException var8) {
					parsedMessage = (SysMessage) var8.getUnfinishedMessage();
					throw var8;
				} finally {
					if (parsedMessage != null) {
						this.mergeFrom(parsedMessage);
					}

				}

				return this;
			}

			public boolean hasVersion() {
				return (this.bitField0_ & 1) == 1;
			}

			public ByteString getVersion() {
				return this.version_;
			}

			public Builder setVersion(ByteString value) {
				if (value == null) {
					throw new NullPointerException();
				} else {
					this.bitField0_ |= 1;
					this.version_ = value;
					this.onChanged();
					return this;
				}
			}

			public Builder clearVersion() {
				this.bitField0_ &= -2;
				this.version_ = SysMessage.getDefaultInstance().getVersion();
				this.onChanged();
				return this;
			}

			public boolean hasSequence() {
				return (this.bitField0_ & 2) == 2;
			}

			public long getSequence() {
				return this.sequence_;
			}

			public Builder setSequence(long value) {
				this.bitField0_ |= 2;
				this.sequence_ = value;
				this.onChanged();
				return this;
			}

			public Builder clearSequence() {
				this.bitField0_ &= -3;
				this.sequence_ = 0L;
				this.onChanged();
				return this;
			}

			public boolean hasMsgId() {
				return (this.bitField0_ & 4) == 4;
			}

			public int getMsgId() {
				return this.msgId_;
			}

			public Builder setMsgId(int value) {
				this.bitField0_ |= 4;
				this.msgId_ = value;
				this.onChanged();
				return this;
			}

			public Builder clearMsgId() {
				this.bitField0_ &= -5;
				this.msgId_ = 0;
				this.onChanged();
				return this;
			}

			public boolean hasInnerMsg() {
				return (this.bitField0_ & 8) == 8;
			}

			public ByteString getInnerMsg() {
				return this.innerMsg_;
			}

			public Builder setInnerMsg(ByteString value) {
				if (value == null) {
					throw new NullPointerException();
				} else {
					this.bitField0_ |= 8;
					this.innerMsg_ = value;
					this.onChanged();
					return this;
				}
			}

			public Builder clearInnerMsg() {
				this.bitField0_ &= -9;
				this.innerMsg_ = SysMessage.getDefaultInstance().getInnerMsg();
				this.onChanged();
				return this;
			}

			private void ensureExtendsIsMutable() {
				if ((this.bitField0_ & 16) != 16) {
					this.extends_ = new ArrayList(this.extends_);
					this.bitField0_ |= 16;
				}

			}

			public List<KStrVPair> getExtendsList() {
				return this.extendsBuilder_ == null ? Collections.unmodifiableList(this.extends_) : this.extendsBuilder_.getMessageList();
			}

			public int getExtendsCount() {
				return this.extendsBuilder_ == null ? this.extends_.size() : this.extendsBuilder_.getCount();
			}

			public KStrVPair getExtends(int index) {
				return this.extendsBuilder_ == null ? (KStrVPair) this.extends_.get(index) : (KStrVPair) this.extendsBuilder_.getMessage(index);
			}

			public Builder setExtends(int index, KStrVPair value) {
				if (this.extendsBuilder_ == null) {
					if (value == null) {
						throw new NullPointerException();
					}

					this.ensureExtendsIsMutable();
					this.extends_.set(index, value);
					this.onChanged();
				} else {
					this.extendsBuilder_.setMessage(index, value);
				}

				return this;
			}

			public Builder setExtends(int index, net.proto.CommonProto.KStrVPair.Builder builderForValue) {
				if (this.extendsBuilder_ == null) {
					this.ensureExtendsIsMutable();
					this.extends_.set(index, builderForValue.build());
					this.onChanged();
				} else {
					this.extendsBuilder_.setMessage(index, builderForValue.build());
				}

				return this;
			}

			public Builder addExtends(KStrVPair value) {
				if (this.extendsBuilder_ == null) {
					if (value == null) {
						throw new NullPointerException();
					}

					this.ensureExtendsIsMutable();
					this.extends_.add(value);
					this.onChanged();
				} else {
					this.extendsBuilder_.addMessage(value);
				}

				return this;
			}

			public Builder addExtends(int index, KStrVPair value) {
				if (this.extendsBuilder_ == null) {
					if (value == null) {
						throw new NullPointerException();
					}

					this.ensureExtendsIsMutable();
					this.extends_.add(index, value);
					this.onChanged();
				} else {
					this.extendsBuilder_.addMessage(index, value);
				}

				return this;
			}

			public Builder addExtends(net.proto.CommonProto.KStrVPair.Builder builderForValue) {
				if (this.extendsBuilder_ == null) {
					this.ensureExtendsIsMutable();
					this.extends_.add(builderForValue.build());
					this.onChanged();
				} else {
					this.extendsBuilder_.addMessage(builderForValue.build());
				}

				return this;
			}

			public Builder addExtends(int index, net.proto.CommonProto.KStrVPair.Builder builderForValue) {
				if (this.extendsBuilder_ == null) {
					this.ensureExtendsIsMutable();
					this.extends_.add(index, builderForValue.build());
					this.onChanged();
				} else {
					this.extendsBuilder_.addMessage(index, builderForValue.build());
				}

				return this;
			}

			public Builder addAllExtends(Iterable<? extends KStrVPair> values) {
				if (this.extendsBuilder_ == null) {
					this.ensureExtendsIsMutable();
					com.google.protobuf.GeneratedMessage.Builder.addAll(values, this.extends_);
					this.onChanged();
				} else {
					this.extendsBuilder_.addAllMessages(values);
				}

				return this;
			}

			public Builder clearExtends() {
				if (this.extendsBuilder_ == null) {
					this.extends_ = Collections.emptyList();
					this.bitField0_ &= -17;
					this.onChanged();
				} else {
					this.extendsBuilder_.clear();
				}

				return this;
			}

			public Builder removeExtends(int index) {
				if (this.extendsBuilder_ == null) {
					this.ensureExtendsIsMutable();
					this.extends_.remove(index);
					this.onChanged();
				} else {
					this.extendsBuilder_.remove(index);
				}

				return this;
			}

			public net.proto.CommonProto.KStrVPair.Builder getExtendsBuilder(int index) {
				return (net.proto.CommonProto.KStrVPair.Builder) this.getExtendsFieldBuilder().getBuilder(index);
			}

			public KStrVPairOrBuilder getExtendsOrBuilder(int index) {
				return this.extendsBuilder_ == null ? (KStrVPairOrBuilder) this.extends_.get(index) : (KStrVPairOrBuilder) this.extendsBuilder_.getMessageOrBuilder(index);
			}

			public List<? extends KStrVPairOrBuilder> getExtendsOrBuilderList() {
				return this.extendsBuilder_ != null ? this.extendsBuilder_.getMessageOrBuilderList() : Collections.unmodifiableList(this.extends_);
			}

			public net.proto.CommonProto.KStrVPair.Builder addExtendsBuilder() {
				return (net.proto.CommonProto.KStrVPair.Builder) this.getExtendsFieldBuilder().addBuilder(KStrVPair.getDefaultInstance());
			}

			public net.proto.CommonProto.KStrVPair.Builder addExtendsBuilder(int index) {
				return (net.proto.CommonProto.KStrVPair.Builder) this.getExtendsFieldBuilder().addBuilder(index, KStrVPair.getDefaultInstance());
			}

			public List<net.proto.CommonProto.KStrVPair.Builder> getExtendsBuilderList() {
				return this.getExtendsFieldBuilder().getBuilderList();
			}

			private RepeatedFieldBuilder<KStrVPair, net.proto.CommonProto.KStrVPair.Builder, KStrVPairOrBuilder> getExtendsFieldBuilder() {
				if (this.extendsBuilder_ == null) {
					this.extendsBuilder_ = new RepeatedFieldBuilder(this.extends_, (this.bitField0_ & 16) == 16, this.getParentForChildren(), this.isClean());
					this.extends_ = null;
				}

				return this.extendsBuilder_;
			}
		}
	}

	public interface SysMessageOrBuilder extends MessageOrBuilder {
		boolean hasVersion();

		ByteString getVersion();

		boolean hasSequence();

		long getSequence();

		boolean hasMsgId();

		int getMsgId();

		boolean hasInnerMsg();

		ByteString getInnerMsg();

		List<KStrVPair> getExtendsList();

		KStrVPair getExtends(int var1);

		int getExtendsCount();

		List<? extends KStrVPairOrBuilder> getExtendsOrBuilderList();

		KStrVPairOrBuilder getExtendsOrBuilder(int var1);
	}
}
