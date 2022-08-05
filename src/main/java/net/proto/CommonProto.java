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
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.UnknownFieldSet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;

public final class CommonProto {
	private static Descriptor internal_static_proto_KVPair_descriptor;
	private static FieldAccessorTable internal_static_proto_KVPair_fieldAccessorTable;
	private static Descriptor internal_static_proto_KStrVPair_descriptor;
	private static FieldAccessorTable internal_static_proto_KStrVPair_fieldAccessorTable;
	private static Descriptor internal_static_proto_Heartbeat_descriptor;
	private static FieldAccessorTable internal_static_proto_Heartbeat_fieldAccessorTable;
	private static Descriptor internal_static_proto_ReqRegister_descriptor;
	private static FieldAccessorTable internal_static_proto_ReqRegister_fieldAccessorTable;
	private static Descriptor internal_static_proto_AckRegister_descriptor;
	private static FieldAccessorTable internal_static_proto_AckRegister_fieldAccessorTable;
	private static FileDescriptor descriptor;

	private CommonProto() {
	}

	public static void registerAllExtensions(ExtensionRegistry registry) {
	}

	public static FileDescriptor getDescriptor() {
		return descriptor;
	}

	static {
		String[] descriptorData = new String[]{"\n\fcommon.proto\u0012\u0005proto\"$\n\u0006KVPair\u0012\u000b\n\u0003key\u0018\u0001 \u0001(\u0003\u0012\r\n\u0005value\u0018\u0002 \u0001(\u0003\"'\n\tKStrVPair\u0012\u000b\n\u0003key\u0018\u0001 \u0001(\u0003\u0012\r\n\u0005value\u0018\u0002 \u0001(\f\"\u001a\n\tHeartbeat\u0012\r\n\u0005times\u0018\u0001 \u0001(\u0003\"Y\n\u000bReqRegister\u0012\u0010\n\bserverId\u0018\u0001 \u0002(\u0003\u0012\u0012\n\nserverType\u0018\u0002 \u0002(\u0005\u0012\u0010\n\bserverIP\u0018\u0003 \u0001(\f\u0012\u0012\n\nserverPort\u0018\u0004 \u0001(\u0005\"U\n\u000bAckRegister\u0012\u000e\n\u0006result\u0018\u0001 \u0002(\u0005\u0012\u0010\n\bserverId\u0018\u0002 \u0002(\u0003\u0012\u0010\n\bserverIP\u0018\u0003 \u0001(\f\u0012\u0012\n\nserverPort\u0018\u0004 \u0001(\u0005B\rB\u000bCommonProto"};
		InternalDescriptorAssigner assigner = new InternalDescriptorAssigner() {
			public ExtensionRegistry assignDescriptors(FileDescriptor root) {
				CommonProto.descriptor = root;
				CommonProto.internal_static_proto_KVPair_descriptor = (Descriptor) CommonProto.getDescriptor().getMessageTypes().get(0);
				CommonProto.internal_static_proto_KVPair_fieldAccessorTable = new FieldAccessorTable(CommonProto.internal_static_proto_KVPair_descriptor, new String[]{"Key", "Value"});
				CommonProto.internal_static_proto_KStrVPair_descriptor = (Descriptor) CommonProto.getDescriptor().getMessageTypes().get(1);
				CommonProto.internal_static_proto_KStrVPair_fieldAccessorTable = new FieldAccessorTable(CommonProto.internal_static_proto_KStrVPair_descriptor, new String[]{"Key", "Value"});
				CommonProto.internal_static_proto_Heartbeat_descriptor = (Descriptor) CommonProto.getDescriptor().getMessageTypes().get(2);
				CommonProto.internal_static_proto_Heartbeat_fieldAccessorTable = new FieldAccessorTable(CommonProto.internal_static_proto_Heartbeat_descriptor, new String[]{"Times"});
				CommonProto.internal_static_proto_ReqRegister_descriptor = (Descriptor) CommonProto.getDescriptor().getMessageTypes().get(3);
				CommonProto.internal_static_proto_ReqRegister_fieldAccessorTable = new FieldAccessorTable(CommonProto.internal_static_proto_ReqRegister_descriptor, new String[]{"ServerId", "ServerType", "ServerIP", "ServerPort"});
				CommonProto.internal_static_proto_AckRegister_descriptor = (Descriptor) CommonProto.getDescriptor().getMessageTypes().get(4);
				CommonProto.internal_static_proto_AckRegister_fieldAccessorTable = new FieldAccessorTable(CommonProto.internal_static_proto_AckRegister_descriptor, new String[]{"Result", "ServerId", "ServerIP", "ServerPort"});
				return null;
			}
		};
		FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new FileDescriptor[0], assigner);
	}

	public static final class AckRegister extends GeneratedMessage implements AckRegisterOrBuilder {
		private static final AckRegister defaultInstance = new AckRegister(true);
		private final UnknownFieldSet unknownFields;
		public static Parser<AckRegister> PARSER = new AbstractParser<AckRegister>() {
			public AckRegister parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
				return new AckRegister(input, extensionRegistry);
			}
		};
		private int bitField0_;
		public static final int RESULT_FIELD_NUMBER = 1;
		private int result_;
		public static final int SERVERID_FIELD_NUMBER = 2;
		private long serverId_;
		public static final int SERVERIP_FIELD_NUMBER = 3;
		private ByteString serverIP_;
		public static final int SERVERPORT_FIELD_NUMBER = 4;
		private int serverPort_;
		private byte memoizedIsInitialized;
		private int memoizedSerializedSize;
		private static final long serialVersionUID = 0L;

		private AckRegister(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
			super(builder);
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.unknownFields = builder.getUnknownFields();
		}

		private AckRegister(boolean noInit) {
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.unknownFields = UnknownFieldSet.getDefaultInstance();
		}

		public static AckRegister getDefaultInstance() {
			return defaultInstance;
		}

		public AckRegister getDefaultInstanceForType() {
			return defaultInstance;
		}

		public final UnknownFieldSet getUnknownFields() {
			return this.unknownFields;
		}

		private AckRegister(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.initFields();
			com.google.protobuf.UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();

			try {
				boolean done = false;

				while (!done) {
					int tag = input.readTag();
					switch (tag) {
						case 0:
							done = true;
							break;
						case 8:
							this.bitField0_ |= 1;
							this.result_ = input.readInt32();
							break;
						case 16:
							this.bitField0_ |= 2;
							this.serverId_ = input.readInt64();
							break;
						case 26:
							this.bitField0_ |= 4;
							this.serverIP_ = input.readBytes();
							break;
						case 32:
							this.bitField0_ |= 8;
							this.serverPort_ = input.readInt32();
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
				this.unknownFields = unknownFields.build();
				this.makeExtensionsImmutable();
			}

		}

		public static final Descriptor getDescriptor() {
			return CommonProto.internal_static_proto_AckRegister_descriptor;
		}

		protected FieldAccessorTable internalGetFieldAccessorTable() {
			return CommonProto.internal_static_proto_AckRegister_fieldAccessorTable.ensureFieldAccessorsInitialized(AckRegister.class, Builder.class);
		}

		public Parser<AckRegister> getParserForType() {
			return PARSER;
		}

		public boolean hasResult() {
			return (this.bitField0_ & 1) == 1;
		}

		public int getResult() {
			return this.result_;
		}

		public boolean hasServerId() {
			return (this.bitField0_ & 2) == 2;
		}

		public long getServerId() {
			return this.serverId_;
		}

		public boolean hasServerIP() {
			return (this.bitField0_ & 4) == 4;
		}

		public ByteString getServerIP() {
			return this.serverIP_;
		}

		public boolean hasServerPort() {
			return (this.bitField0_ & 8) == 8;
		}

		public int getServerPort() {
			return this.serverPort_;
		}

		private void initFields() {
			this.result_ = 0;
			this.serverId_ = 0L;
			this.serverIP_ = ByteString.EMPTY;
			this.serverPort_ = 0;
		}

		public final boolean isInitialized() {
			byte isInitialized = this.memoizedIsInitialized;
			if (isInitialized != -1) {
				return isInitialized == 1;
			} else if (!this.hasResult()) {
				this.memoizedIsInitialized = 0;
				return false;
			} else if (!this.hasServerId()) {
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
				output.writeInt32(1, this.result_);
			}

			if ((this.bitField0_ & 2) == 2) {
				output.writeInt64(2, this.serverId_);
			}

			if ((this.bitField0_ & 4) == 4) {
				output.writeBytes(3, this.serverIP_);
			}

			if ((this.bitField0_ & 8) == 8) {
				output.writeInt32(4, this.serverPort_);
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
					size += CodedOutputStream.computeInt32Size(1, this.result_);
				}

				if ((this.bitField0_ & 2) == 2) {
					size += CodedOutputStream.computeInt64Size(2, this.serverId_);
				}

				if ((this.bitField0_ & 4) == 4) {
					size += CodedOutputStream.computeBytesSize(3, this.serverIP_);
				}

				if ((this.bitField0_ & 8) == 8) {
					size += CodedOutputStream.computeInt32Size(4, this.serverPort_);
				}

				size += this.getUnknownFields().getSerializedSize();
				this.memoizedSerializedSize = size;
				return size;
			}
		}

		protected Object writeReplace() throws ObjectStreamException {
			return super.writeReplace();
		}

		public static AckRegister parseFrom(ByteString data) throws InvalidProtocolBufferException {
			return (AckRegister) PARSER.parseFrom(data);
		}

		public static AckRegister parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			return (AckRegister) PARSER.parseFrom(data, extensionRegistry);
		}

		public static AckRegister parseFrom(byte[] data) throws InvalidProtocolBufferException {
			return (AckRegister) PARSER.parseFrom(data);
		}

		public static AckRegister parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			return (AckRegister) PARSER.parseFrom(data, extensionRegistry);
		}

		public static AckRegister parseFrom(InputStream input) throws IOException {
			return (AckRegister) PARSER.parseFrom(input);
		}

		public static AckRegister parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (AckRegister) PARSER.parseFrom(input, extensionRegistry);
		}

		public static AckRegister parseDelimitedFrom(InputStream input) throws IOException {
			return (AckRegister) PARSER.parseDelimitedFrom(input);
		}

		public static AckRegister parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (AckRegister) PARSER.parseDelimitedFrom(input, extensionRegistry);
		}

		public static AckRegister parseFrom(CodedInputStream input) throws IOException {
			return (AckRegister) PARSER.parseFrom(input);
		}

		public static AckRegister parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (AckRegister) PARSER.parseFrom(input, extensionRegistry);
		}

		public static Builder newBuilder() {
			return Builder.create();
		}

		public Builder newBuilderForType() {
			return newBuilder();
		}

		public static Builder newBuilder(AckRegister prototype) {
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

		public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements AckRegisterOrBuilder {
			private int bitField0_;
			private int result_;
			private long serverId_;
			private ByteString serverIP_;
			private int serverPort_;

			public static final Descriptor getDescriptor() {
				return CommonProto.internal_static_proto_AckRegister_descriptor;
			}

			protected FieldAccessorTable internalGetFieldAccessorTable() {
				return CommonProto.internal_static_proto_AckRegister_fieldAccessorTable.ensureFieldAccessorsInitialized(AckRegister.class, Builder.class);
			}

			private Builder() {
				this.serverIP_ = ByteString.EMPTY;
				this.maybeForceBuilderInitialization();
			}

			private Builder(BuilderParent parent) {
				super(parent);
				this.serverIP_ = ByteString.EMPTY;
				this.maybeForceBuilderInitialization();
			}

			private void maybeForceBuilderInitialization() {
				if (AckRegister.alwaysUseFieldBuilders) {
				}

			}

			private static Builder create() {
				return new Builder();
			}

			public Builder clear() {
				super.clear();
				this.result_ = 0;
				this.bitField0_ &= -2;
				this.serverId_ = 0L;
				this.bitField0_ &= -3;
				this.serverIP_ = ByteString.EMPTY;
				this.bitField0_ &= -5;
				this.serverPort_ = 0;
				this.bitField0_ &= -9;
				return this;
			}

			public Builder clone() {
				return create().mergeFrom(this.buildPartial());
			}

			public Descriptor getDescriptorForType() {
				return CommonProto.internal_static_proto_AckRegister_descriptor;
			}

			public AckRegister getDefaultInstanceForType() {
				return AckRegister.getDefaultInstance();
			}

			public AckRegister build() {
				AckRegister result = this.buildPartial();
				if (!result.isInitialized()) {
					throw newUninitializedMessageException(result);
				} else {
					return result;
				}
			}

			public AckRegister buildPartial() {
				AckRegister result = new AckRegister(this);
				int from_bitField0_ = this.bitField0_;
				int to_bitField0_ = 0;
				if ((from_bitField0_ & 1) == 1) {
					to_bitField0_ |= 1;
				}

				result.result_ = this.result_;
				if ((from_bitField0_ & 2) == 2) {
					to_bitField0_ |= 2;
				}

				result.serverId_ = this.serverId_;
				if ((from_bitField0_ & 4) == 4) {
					to_bitField0_ |= 4;
				}

				result.serverIP_ = this.serverIP_;
				if ((from_bitField0_ & 8) == 8) {
					to_bitField0_ |= 8;
				}

				result.serverPort_ = this.serverPort_;
				result.bitField0_ = to_bitField0_;
				this.onBuilt();
				return result;
			}

			public Builder mergeFrom(Message other) {
				if (other instanceof AckRegister) {
					return this.mergeFrom((AckRegister) other);
				} else {
					super.mergeFrom(other);
					return this;
				}
			}

			public Builder mergeFrom(AckRegister other) {
				if (other == AckRegister.getDefaultInstance()) {
					return this;
				} else {
					if (other.hasResult()) {
						this.setResult(other.getResult());
					}

					if (other.hasServerId()) {
						this.setServerId(other.getServerId());
					}

					if (other.hasServerIP()) {
						this.setServerIP(other.getServerIP());
					}

					if (other.hasServerPort()) {
						this.setServerPort(other.getServerPort());
					}

					this.mergeUnknownFields(other.getUnknownFields());
					return this;
				}
			}

			public final boolean isInitialized() {
				if (!this.hasResult()) {
					return false;
				} else {
					return this.hasServerId();
				}
			}

			public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
				AckRegister parsedMessage = null;

				try {
					parsedMessage = (AckRegister) AckRegister.PARSER.parsePartialFrom(input, extensionRegistry);
				} catch (InvalidProtocolBufferException var8) {
					parsedMessage = (AckRegister) var8.getUnfinishedMessage();
					throw var8;
				} finally {
					if (parsedMessage != null) {
						this.mergeFrom(parsedMessage);
					}

				}

				return this;
			}

			public boolean hasResult() {
				return (this.bitField0_ & 1) == 1;
			}

			public int getResult() {
				return this.result_;
			}

			public Builder setResult(int value) {
				this.bitField0_ |= 1;
				this.result_ = value;
				this.onChanged();
				return this;
			}

			public Builder clearResult() {
				this.bitField0_ &= -2;
				this.result_ = 0;
				this.onChanged();
				return this;
			}

			public boolean hasServerId() {
				return (this.bitField0_ & 2) == 2;
			}

			public long getServerId() {
				return this.serverId_;
			}

			public Builder setServerId(long value) {
				this.bitField0_ |= 2;
				this.serverId_ = value;
				this.onChanged();
				return this;
			}

			public Builder clearServerId() {
				this.bitField0_ &= -3;
				this.serverId_ = 0L;
				this.onChanged();
				return this;
			}

			public boolean hasServerIP() {
				return (this.bitField0_ & 4) == 4;
			}

			public ByteString getServerIP() {
				return this.serverIP_;
			}

			public Builder setServerIP(ByteString value) {
				if (value == null) {
					throw new NullPointerException();
				} else {
					this.bitField0_ |= 4;
					this.serverIP_ = value;
					this.onChanged();
					return this;
				}
			}

			public Builder clearServerIP() {
				this.bitField0_ &= -5;
				this.serverIP_ = AckRegister.getDefaultInstance().getServerIP();
				this.onChanged();
				return this;
			}

			public boolean hasServerPort() {
				return (this.bitField0_ & 8) == 8;
			}

			public int getServerPort() {
				return this.serverPort_;
			}

			public Builder setServerPort(int value) {
				this.bitField0_ |= 8;
				this.serverPort_ = value;
				this.onChanged();
				return this;
			}

			public Builder clearServerPort() {
				this.bitField0_ &= -9;
				this.serverPort_ = 0;
				this.onChanged();
				return this;
			}
		}
	}

	public interface AckRegisterOrBuilder extends MessageOrBuilder {
		boolean hasResult();

		int getResult();

		boolean hasServerId();

		long getServerId();

		boolean hasServerIP();

		ByteString getServerIP();

		boolean hasServerPort();

		int getServerPort();
	}

	public static final class ReqRegister extends GeneratedMessage implements ReqRegisterOrBuilder {
		private static final ReqRegister defaultInstance = new ReqRegister(true);
		private final UnknownFieldSet unknownFields;
		public static Parser<ReqRegister> PARSER = new AbstractParser<ReqRegister>() {
			public ReqRegister parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
				return new ReqRegister(input, extensionRegistry);
			}
		};
		private int bitField0_;
		public static final int SERVERID_FIELD_NUMBER = 1;
		private long serverId_;
		public static final int SERVERTYPE_FIELD_NUMBER = 2;
		private int serverType_;
		public static final int SERVERIP_FIELD_NUMBER = 3;
		private ByteString serverIP_;
		public static final int SERVERPORT_FIELD_NUMBER = 4;
		private int serverPort_;
		private byte memoizedIsInitialized;
		private int memoizedSerializedSize;
		private static final long serialVersionUID = 0L;

		private ReqRegister(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
			super(builder);
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.unknownFields = builder.getUnknownFields();
		}

		private ReqRegister(boolean noInit) {
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.unknownFields = UnknownFieldSet.getDefaultInstance();
		}

		public static ReqRegister getDefaultInstance() {
			return defaultInstance;
		}

		public ReqRegister getDefaultInstanceForType() {
			return defaultInstance;
		}

		public final UnknownFieldSet getUnknownFields() {
			return this.unknownFields;
		}

		private ReqRegister(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.initFields();
			com.google.protobuf.UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();

			try {
				boolean done = false;

				while (!done) {
					int tag = input.readTag();
					switch (tag) {
						case 0:
							done = true;
							break;
						case 8:
							this.bitField0_ |= 1;
							this.serverId_ = input.readInt64();
							break;
						case 16:
							this.bitField0_ |= 2;
							this.serverType_ = input.readInt32();
							break;
						case 26:
							this.bitField0_ |= 4;
							this.serverIP_ = input.readBytes();
							break;
						case 32:
							this.bitField0_ |= 8;
							this.serverPort_ = input.readInt32();
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
				this.unknownFields = unknownFields.build();
				this.makeExtensionsImmutable();
			}

		}

		public static final Descriptor getDescriptor() {
			return CommonProto.internal_static_proto_ReqRegister_descriptor;
		}

		protected FieldAccessorTable internalGetFieldAccessorTable() {
			return CommonProto.internal_static_proto_ReqRegister_fieldAccessorTable.ensureFieldAccessorsInitialized(ReqRegister.class, Builder.class);
		}

		public Parser<ReqRegister> getParserForType() {
			return PARSER;
		}

		public boolean hasServerId() {
			return (this.bitField0_ & 1) == 1;
		}

		public long getServerId() {
			return this.serverId_;
		}

		public boolean hasServerType() {
			return (this.bitField0_ & 2) == 2;
		}

		public int getServerType() {
			return this.serverType_;
		}

		public boolean hasServerIP() {
			return (this.bitField0_ & 4) == 4;
		}

		public ByteString getServerIP() {
			return this.serverIP_;
		}

		public boolean hasServerPort() {
			return (this.bitField0_ & 8) == 8;
		}

		public int getServerPort() {
			return this.serverPort_;
		}

		private void initFields() {
			this.serverId_ = 0L;
			this.serverType_ = 0;
			this.serverIP_ = ByteString.EMPTY;
			this.serverPort_ = 0;
		}

		public final boolean isInitialized() {
			byte isInitialized = this.memoizedIsInitialized;
			if (isInitialized != -1) {
				return isInitialized == 1;
			} else if (!this.hasServerId()) {
				this.memoizedIsInitialized = 0;
				return false;
			} else if (!this.hasServerType()) {
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
				output.writeInt64(1, this.serverId_);
			}

			if ((this.bitField0_ & 2) == 2) {
				output.writeInt32(2, this.serverType_);
			}

			if ((this.bitField0_ & 4) == 4) {
				output.writeBytes(3, this.serverIP_);
			}

			if ((this.bitField0_ & 8) == 8) {
				output.writeInt32(4, this.serverPort_);
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
					size += CodedOutputStream.computeInt64Size(1, this.serverId_);
				}

				if ((this.bitField0_ & 2) == 2) {
					size += CodedOutputStream.computeInt32Size(2, this.serverType_);
				}

				if ((this.bitField0_ & 4) == 4) {
					size += CodedOutputStream.computeBytesSize(3, this.serverIP_);
				}

				if ((this.bitField0_ & 8) == 8) {
					size += CodedOutputStream.computeInt32Size(4, this.serverPort_);
				}

				size += this.getUnknownFields().getSerializedSize();
				this.memoizedSerializedSize = size;
				return size;
			}
		}

		protected Object writeReplace() throws ObjectStreamException {
			return super.writeReplace();
		}

		public static ReqRegister parseFrom(ByteString data) throws InvalidProtocolBufferException {
			return (ReqRegister) PARSER.parseFrom(data);
		}

		public static ReqRegister parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			return (ReqRegister) PARSER.parseFrom(data, extensionRegistry);
		}

		public static ReqRegister parseFrom(byte[] data) throws InvalidProtocolBufferException {
			return (ReqRegister) PARSER.parseFrom(data);
		}

		public static ReqRegister parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			return (ReqRegister) PARSER.parseFrom(data, extensionRegistry);
		}

		public static ReqRegister parseFrom(InputStream input) throws IOException {
			return (ReqRegister) PARSER.parseFrom(input);
		}

		public static ReqRegister parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (ReqRegister) PARSER.parseFrom(input, extensionRegistry);
		}

		public static ReqRegister parseDelimitedFrom(InputStream input) throws IOException {
			return (ReqRegister) PARSER.parseDelimitedFrom(input);
		}

		public static ReqRegister parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (ReqRegister) PARSER.parseDelimitedFrom(input, extensionRegistry);
		}

		public static ReqRegister parseFrom(CodedInputStream input) throws IOException {
			return (ReqRegister) PARSER.parseFrom(input);
		}

		public static ReqRegister parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (ReqRegister) PARSER.parseFrom(input, extensionRegistry);
		}

		public static Builder newBuilder() {
			return Builder.create();
		}

		public Builder newBuilderForType() {
			return newBuilder();
		}

		public static Builder newBuilder(ReqRegister prototype) {
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

		public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements ReqRegisterOrBuilder {
			private int bitField0_;
			private long serverId_;
			private int serverType_;
			private ByteString serverIP_;
			private int serverPort_;

			public static final Descriptor getDescriptor() {
				return CommonProto.internal_static_proto_ReqRegister_descriptor;
			}

			protected FieldAccessorTable internalGetFieldAccessorTable() {
				return CommonProto.internal_static_proto_ReqRegister_fieldAccessorTable.ensureFieldAccessorsInitialized(ReqRegister.class, Builder.class);
			}

			private Builder() {
				this.serverIP_ = ByteString.EMPTY;
				this.maybeForceBuilderInitialization();
			}

			private Builder(BuilderParent parent) {
				super(parent);
				this.serverIP_ = ByteString.EMPTY;
				this.maybeForceBuilderInitialization();
			}

			private void maybeForceBuilderInitialization() {
				if (ReqRegister.alwaysUseFieldBuilders) {
				}

			}

			private static Builder create() {
				return new Builder();
			}

			public Builder clear() {
				super.clear();
				this.serverId_ = 0L;
				this.bitField0_ &= -2;
				this.serverType_ = 0;
				this.bitField0_ &= -3;
				this.serverIP_ = ByteString.EMPTY;
				this.bitField0_ &= -5;
				this.serverPort_ = 0;
				this.bitField0_ &= -9;
				return this;
			}

			public Builder clone() {
				return create().mergeFrom(this.buildPartial());
			}

			public Descriptor getDescriptorForType() {
				return CommonProto.internal_static_proto_ReqRegister_descriptor;
			}

			public ReqRegister getDefaultInstanceForType() {
				return ReqRegister.getDefaultInstance();
			}

			public ReqRegister build() {
				ReqRegister result = this.buildPartial();
				if (!result.isInitialized()) {
					throw newUninitializedMessageException(result);
				} else {
					return result;
				}
			}

			public ReqRegister buildPartial() {
				ReqRegister result = new ReqRegister(this);
				int from_bitField0_ = this.bitField0_;
				int to_bitField0_ = 0;
				if ((from_bitField0_ & 1) == 1) {
					to_bitField0_ |= 1;
				}

				result.serverId_ = this.serverId_;
				if ((from_bitField0_ & 2) == 2) {
					to_bitField0_ |= 2;
				}

				result.serverType_ = this.serverType_;
				if ((from_bitField0_ & 4) == 4) {
					to_bitField0_ |= 4;
				}

				result.serverIP_ = this.serverIP_;
				if ((from_bitField0_ & 8) == 8) {
					to_bitField0_ |= 8;
				}

				result.serverPort_ = this.serverPort_;
				result.bitField0_ = to_bitField0_;
				this.onBuilt();
				return result;
			}

			public Builder mergeFrom(Message other) {
				if (other instanceof ReqRegister) {
					return this.mergeFrom((ReqRegister) other);
				} else {
					super.mergeFrom(other);
					return this;
				}
			}

			public Builder mergeFrom(ReqRegister other) {
				if (other == ReqRegister.getDefaultInstance()) {
					return this;
				} else {
					if (other.hasServerId()) {
						this.setServerId(other.getServerId());
					}

					if (other.hasServerType()) {
						this.setServerType(other.getServerType());
					}

					if (other.hasServerIP()) {
						this.setServerIP(other.getServerIP());
					}

					if (other.hasServerPort()) {
						this.setServerPort(other.getServerPort());
					}

					this.mergeUnknownFields(other.getUnknownFields());
					return this;
				}
			}

			public final boolean isInitialized() {
				if (!this.hasServerId()) {
					return false;
				} else {
					return this.hasServerType();
				}
			}

			public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
				ReqRegister parsedMessage = null;

				try {
					parsedMessage = (ReqRegister) ReqRegister.PARSER.parsePartialFrom(input, extensionRegistry);
				} catch (InvalidProtocolBufferException var8) {
					parsedMessage = (ReqRegister) var8.getUnfinishedMessage();
					throw var8;
				} finally {
					if (parsedMessage != null) {
						this.mergeFrom(parsedMessage);
					}

				}

				return this;
			}

			public boolean hasServerId() {
				return (this.bitField0_ & 1) == 1;
			}

			public long getServerId() {
				return this.serverId_;
			}

			public Builder setServerId(long value) {
				this.bitField0_ |= 1;
				this.serverId_ = value;
				this.onChanged();
				return this;
			}

			public Builder clearServerId() {
				this.bitField0_ &= -2;
				this.serverId_ = 0L;
				this.onChanged();
				return this;
			}

			public boolean hasServerType() {
				return (this.bitField0_ & 2) == 2;
			}

			public int getServerType() {
				return this.serverType_;
			}

			public Builder setServerType(int value) {
				this.bitField0_ |= 2;
				this.serverType_ = value;
				this.onChanged();
				return this;
			}

			public Builder clearServerType() {
				this.bitField0_ &= -3;
				this.serverType_ = 0;
				this.onChanged();
				return this;
			}

			public boolean hasServerIP() {
				return (this.bitField0_ & 4) == 4;
			}

			public ByteString getServerIP() {
				return this.serverIP_;
			}

			public Builder setServerIP(ByteString value) {
				if (value == null) {
					throw new NullPointerException();
				} else {
					this.bitField0_ |= 4;
					this.serverIP_ = value;
					this.onChanged();
					return this;
				}
			}

			public Builder clearServerIP() {
				this.bitField0_ &= -5;
				this.serverIP_ = ReqRegister.getDefaultInstance().getServerIP();
				this.onChanged();
				return this;
			}

			public boolean hasServerPort() {
				return (this.bitField0_ & 8) == 8;
			}

			public int getServerPort() {
				return this.serverPort_;
			}

			public Builder setServerPort(int value) {
				this.bitField0_ |= 8;
				this.serverPort_ = value;
				this.onChanged();
				return this;
			}

			public Builder clearServerPort() {
				this.bitField0_ &= -9;
				this.serverPort_ = 0;
				this.onChanged();
				return this;
			}
		}
	}

	public interface ReqRegisterOrBuilder extends MessageOrBuilder {
		boolean hasServerId();

		long getServerId();

		boolean hasServerType();

		int getServerType();

		boolean hasServerIP();

		ByteString getServerIP();

		boolean hasServerPort();

		int getServerPort();
	}

	public static final class Heartbeat extends GeneratedMessage implements HeartbeatOrBuilder {
		private static final Heartbeat defaultInstance = new Heartbeat(true);
		private final UnknownFieldSet unknownFields;
		public static Parser<Heartbeat> PARSER = new AbstractParser<Heartbeat>() {
			public Heartbeat parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
				return new Heartbeat(input, extensionRegistry);
			}
		};
		private int bitField0_;
		public static final int TIMES_FIELD_NUMBER = 1;
		private long times_;
		private byte memoizedIsInitialized;
		private int memoizedSerializedSize;
		private static final long serialVersionUID = 0L;

		private Heartbeat(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
			super(builder);
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.unknownFields = builder.getUnknownFields();
		}

		private Heartbeat(boolean noInit) {
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.unknownFields = UnknownFieldSet.getDefaultInstance();
		}

		public static Heartbeat getDefaultInstance() {
			return defaultInstance;
		}

		public Heartbeat getDefaultInstanceForType() {
			return defaultInstance;
		}

		public final UnknownFieldSet getUnknownFields() {
			return this.unknownFields;
		}

		private Heartbeat(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.initFields();
			com.google.protobuf.UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();

			try {
				boolean done = false;

				while (!done) {
					int tag = input.readTag();
					switch (tag) {
						case 0:
							done = true;
							break;
						case 8:
							this.bitField0_ |= 1;
							this.times_ = input.readInt64();
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
				this.unknownFields = unknownFields.build();
				this.makeExtensionsImmutable();
			}

		}

		public static final Descriptor getDescriptor() {
			return CommonProto.internal_static_proto_Heartbeat_descriptor;
		}

		protected FieldAccessorTable internalGetFieldAccessorTable() {
			return CommonProto.internal_static_proto_Heartbeat_fieldAccessorTable.ensureFieldAccessorsInitialized(Heartbeat.class, Builder.class);
		}

		public Parser<Heartbeat> getParserForType() {
			return PARSER;
		}

		public boolean hasTimes() {
			return (this.bitField0_ & 1) == 1;
		}

		public long getTimes() {
			return this.times_;
		}

		private void initFields() {
			this.times_ = 0L;
		}

		public final boolean isInitialized() {
			byte isInitialized = this.memoizedIsInitialized;
			if (isInitialized != -1) {
				return isInitialized == 1;
			} else {
				this.memoizedIsInitialized = 1;
				return true;
			}
		}

		public void writeTo(CodedOutputStream output) throws IOException {
			this.getSerializedSize();
			if ((this.bitField0_ & 1) == 1) {
				output.writeInt64(1, this.times_);
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
					size += CodedOutputStream.computeInt64Size(1, this.times_);
				}

				size += this.getUnknownFields().getSerializedSize();
				this.memoizedSerializedSize = size;
				return size;
			}
		}

		protected Object writeReplace() throws ObjectStreamException {
			return super.writeReplace();
		}

		public static Heartbeat parseFrom(ByteString data) throws InvalidProtocolBufferException {
			return (Heartbeat) PARSER.parseFrom(data);
		}

		public static Heartbeat parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			return (Heartbeat) PARSER.parseFrom(data, extensionRegistry);
		}

		public static Heartbeat parseFrom(byte[] data) throws InvalidProtocolBufferException {
			return (Heartbeat) PARSER.parseFrom(data);
		}

		public static Heartbeat parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			return (Heartbeat) PARSER.parseFrom(data, extensionRegistry);
		}

		public static Heartbeat parseFrom(InputStream input) throws IOException {
			return (Heartbeat) PARSER.parseFrom(input);
		}

		public static Heartbeat parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (Heartbeat) PARSER.parseFrom(input, extensionRegistry);
		}

		public static Heartbeat parseDelimitedFrom(InputStream input) throws IOException {
			return (Heartbeat) PARSER.parseDelimitedFrom(input);
		}

		public static Heartbeat parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (Heartbeat) PARSER.parseDelimitedFrom(input, extensionRegistry);
		}

		public static Heartbeat parseFrom(CodedInputStream input) throws IOException {
			return (Heartbeat) PARSER.parseFrom(input);
		}

		public static Heartbeat parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (Heartbeat) PARSER.parseFrom(input, extensionRegistry);
		}

		public static Builder newBuilder() {
			return Builder.create();
		}

		public Builder newBuilderForType() {
			return newBuilder();
		}

		public static Builder newBuilder(Heartbeat prototype) {
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

		public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements HeartbeatOrBuilder {
			private int bitField0_;
			private long times_;

			public static final Descriptor getDescriptor() {
				return CommonProto.internal_static_proto_Heartbeat_descriptor;
			}

			protected FieldAccessorTable internalGetFieldAccessorTable() {
				return CommonProto.internal_static_proto_Heartbeat_fieldAccessorTable.ensureFieldAccessorsInitialized(Heartbeat.class, Builder.class);
			}

			private Builder() {
				this.maybeForceBuilderInitialization();
			}

			private Builder(BuilderParent parent) {
				super(parent);
				this.maybeForceBuilderInitialization();
			}

			private void maybeForceBuilderInitialization() {
				if (Heartbeat.alwaysUseFieldBuilders) {
				}

			}

			private static Builder create() {
				return new Builder();
			}

			public Builder clear() {
				super.clear();
				this.times_ = 0L;
				this.bitField0_ &= -2;
				return this;
			}

			public Builder clone() {
				return create().mergeFrom(this.buildPartial());
			}

			public Descriptor getDescriptorForType() {
				return CommonProto.internal_static_proto_Heartbeat_descriptor;
			}

			public Heartbeat getDefaultInstanceForType() {
				return Heartbeat.getDefaultInstance();
			}

			public Heartbeat build() {
				Heartbeat result = this.buildPartial();
				if (!result.isInitialized()) {
					throw newUninitializedMessageException(result);
				} else {
					return result;
				}
			}

			public Heartbeat buildPartial() {
				Heartbeat result = new Heartbeat(this);
				int from_bitField0_ = this.bitField0_;
				int to_bitField0_ = 0;
				if ((from_bitField0_ & 1) == 1) {
					to_bitField0_ |= 1;
				}

				result.times_ = this.times_;
				result.bitField0_ = to_bitField0_;
				this.onBuilt();
				return result;
			}

			public Builder mergeFrom(Message other) {
				if (other instanceof Heartbeat) {
					return this.mergeFrom((Heartbeat) other);
				} else {
					super.mergeFrom(other);
					return this;
				}
			}

			public Builder mergeFrom(Heartbeat other) {
				if (other == Heartbeat.getDefaultInstance()) {
					return this;
				} else {
					if (other.hasTimes()) {
						this.setTimes(other.getTimes());
					}

					this.mergeUnknownFields(other.getUnknownFields());
					return this;
				}
			}

			public final boolean isInitialized() {
				return true;
			}

			public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
				Heartbeat parsedMessage = null;

				try {
					parsedMessage = (Heartbeat) Heartbeat.PARSER.parsePartialFrom(input, extensionRegistry);
				} catch (InvalidProtocolBufferException var8) {
					parsedMessage = (Heartbeat) var8.getUnfinishedMessage();
					throw var8;
				} finally {
					if (parsedMessage != null) {
						this.mergeFrom(parsedMessage);
					}

				}

				return this;
			}

			public boolean hasTimes() {
				return (this.bitField0_ & 1) == 1;
			}

			public long getTimes() {
				return this.times_;
			}

			public Builder setTimes(long value) {
				this.bitField0_ |= 1;
				this.times_ = value;
				this.onChanged();
				return this;
			}

			public Builder clearTimes() {
				this.bitField0_ &= -2;
				this.times_ = 0L;
				this.onChanged();
				return this;
			}
		}
	}

	public interface HeartbeatOrBuilder extends MessageOrBuilder {
		boolean hasTimes();

		long getTimes();
	}

	public static final class KStrVPair extends GeneratedMessage implements KStrVPairOrBuilder {
		private static final KStrVPair defaultInstance = new KStrVPair(true);
		private final UnknownFieldSet unknownFields;
		public static Parser<KStrVPair> PARSER = new AbstractParser<KStrVPair>() {
			public KStrVPair parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
				return new KStrVPair(input, extensionRegistry);
			}
		};
		private int bitField0_;
		public static final int KEY_FIELD_NUMBER = 1;
		private long key_;
		public static final int VALUE_FIELD_NUMBER = 2;
		private ByteString value_;
		private byte memoizedIsInitialized;
		private int memoizedSerializedSize;
		private static final long serialVersionUID = 0L;

		private KStrVPair(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
			super(builder);
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.unknownFields = builder.getUnknownFields();
		}

		private KStrVPair(boolean noInit) {
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.unknownFields = UnknownFieldSet.getDefaultInstance();
		}

		public static KStrVPair getDefaultInstance() {
			return defaultInstance;
		}

		public KStrVPair getDefaultInstanceForType() {
			return defaultInstance;
		}

		public final UnknownFieldSet getUnknownFields() {
			return this.unknownFields;
		}

		private KStrVPair(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.initFields();
			com.google.protobuf.UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();

			try {
				boolean done = false;

				while (!done) {
					int tag = input.readTag();
					switch (tag) {
						case 0:
							done = true;
							break;
						case 8:
							this.bitField0_ |= 1;
							this.key_ = input.readInt64();
							break;
						case 18:
							this.bitField0_ |= 2;
							this.value_ = input.readBytes();
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
				this.unknownFields = unknownFields.build();
				this.makeExtensionsImmutable();
			}

		}

		public static final Descriptor getDescriptor() {
			return CommonProto.internal_static_proto_KStrVPair_descriptor;
		}

		protected FieldAccessorTable internalGetFieldAccessorTable() {
			return CommonProto.internal_static_proto_KStrVPair_fieldAccessorTable.ensureFieldAccessorsInitialized(KStrVPair.class, Builder.class);
		}

		public Parser<KStrVPair> getParserForType() {
			return PARSER;
		}

		public boolean hasKey() {
			return (this.bitField0_ & 1) == 1;
		}

		public long getKey() {
			return this.key_;
		}

		public boolean hasValue() {
			return (this.bitField0_ & 2) == 2;
		}

		public ByteString getValue() {
			return this.value_;
		}

		private void initFields() {
			this.key_ = 0L;
			this.value_ = ByteString.EMPTY;
		}

		public final boolean isInitialized() {
			byte isInitialized = this.memoizedIsInitialized;
			if (isInitialized != -1) {
				return isInitialized == 1;
			} else {
				this.memoizedIsInitialized = 1;
				return true;
			}
		}

		public void writeTo(CodedOutputStream output) throws IOException {
			this.getSerializedSize();
			if ((this.bitField0_ & 1) == 1) {
				output.writeInt64(1, this.key_);
			}

			if ((this.bitField0_ & 2) == 2) {
				output.writeBytes(2, this.value_);
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
					size += CodedOutputStream.computeInt64Size(1, this.key_);
				}

				if ((this.bitField0_ & 2) == 2) {
					size += CodedOutputStream.computeBytesSize(2, this.value_);
				}

				size += this.getUnknownFields().getSerializedSize();
				this.memoizedSerializedSize = size;
				return size;
			}
		}

		protected Object writeReplace() throws ObjectStreamException {
			return super.writeReplace();
		}

		public static KStrVPair parseFrom(ByteString data) throws InvalidProtocolBufferException {
			return (KStrVPair) PARSER.parseFrom(data);
		}

		public static KStrVPair parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			return (KStrVPair) PARSER.parseFrom(data, extensionRegistry);
		}

		public static KStrVPair parseFrom(byte[] data) throws InvalidProtocolBufferException {
			return (KStrVPair) PARSER.parseFrom(data);
		}

		public static KStrVPair parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			return (KStrVPair) PARSER.parseFrom(data, extensionRegistry);
		}

		public static KStrVPair parseFrom(InputStream input) throws IOException {
			return (KStrVPair) PARSER.parseFrom(input);
		}

		public static KStrVPair parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (KStrVPair) PARSER.parseFrom(input, extensionRegistry);
		}

		public static KStrVPair parseDelimitedFrom(InputStream input) throws IOException {
			return (KStrVPair) PARSER.parseDelimitedFrom(input);
		}

		public static KStrVPair parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (KStrVPair) PARSER.parseDelimitedFrom(input, extensionRegistry);
		}

		public static KStrVPair parseFrom(CodedInputStream input) throws IOException {
			return (KStrVPair) PARSER.parseFrom(input);
		}

		public static KStrVPair parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (KStrVPair) PARSER.parseFrom(input, extensionRegistry);
		}

		public static Builder newBuilder() {
			return Builder.create();
		}

		public Builder newBuilderForType() {
			return newBuilder();
		}

		public static Builder newBuilder(KStrVPair prototype) {
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

		public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements KStrVPairOrBuilder {
			private int bitField0_;
			private long key_;
			private ByteString value_;

			public static final Descriptor getDescriptor() {
				return CommonProto.internal_static_proto_KStrVPair_descriptor;
			}

			protected FieldAccessorTable internalGetFieldAccessorTable() {
				return CommonProto.internal_static_proto_KStrVPair_fieldAccessorTable.ensureFieldAccessorsInitialized(KStrVPair.class, Builder.class);
			}

			private Builder() {
				this.value_ = ByteString.EMPTY;
				this.maybeForceBuilderInitialization();
			}

			private Builder(BuilderParent parent) {
				super(parent);
				this.value_ = ByteString.EMPTY;
				this.maybeForceBuilderInitialization();
			}

			private void maybeForceBuilderInitialization() {
				if (KStrVPair.alwaysUseFieldBuilders) {
				}

			}

			private static Builder create() {
				return new Builder();
			}

			public Builder clear() {
				super.clear();
				this.key_ = 0L;
				this.bitField0_ &= -2;
				this.value_ = ByteString.EMPTY;
				this.bitField0_ &= -3;
				return this;
			}

			public Builder clone() {
				return create().mergeFrom(this.buildPartial());
			}

			public Descriptor getDescriptorForType() {
				return CommonProto.internal_static_proto_KStrVPair_descriptor;
			}

			public KStrVPair getDefaultInstanceForType() {
				return KStrVPair.getDefaultInstance();
			}

			public KStrVPair build() {
				KStrVPair result = this.buildPartial();
				if (!result.isInitialized()) {
					throw newUninitializedMessageException(result);
				} else {
					return result;
				}
			}

			public KStrVPair buildPartial() {
				KStrVPair result = new KStrVPair(this);
				int from_bitField0_ = this.bitField0_;
				int to_bitField0_ = 0;
				if ((from_bitField0_ & 1) == 1) {
					to_bitField0_ |= 1;
				}

				result.key_ = this.key_;
				if ((from_bitField0_ & 2) == 2) {
					to_bitField0_ |= 2;
				}

				result.value_ = this.value_;
				result.bitField0_ = to_bitField0_;
				this.onBuilt();
				return result;
			}

			public Builder mergeFrom(Message other) {
				if (other instanceof KStrVPair) {
					return this.mergeFrom((KStrVPair) other);
				} else {
					super.mergeFrom(other);
					return this;
				}
			}

			public Builder mergeFrom(KStrVPair other) {
				if (other == KStrVPair.getDefaultInstance()) {
					return this;
				} else {
					if (other.hasKey()) {
						this.setKey(other.getKey());
					}

					if (other.hasValue()) {
						this.setValue(other.getValue());
					}

					this.mergeUnknownFields(other.getUnknownFields());
					return this;
				}
			}

			public final boolean isInitialized() {
				return true;
			}

			public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
				KStrVPair parsedMessage = null;

				try {
					parsedMessage = (KStrVPair) KStrVPair.PARSER.parsePartialFrom(input, extensionRegistry);
				} catch (InvalidProtocolBufferException var8) {
					parsedMessage = (KStrVPair) var8.getUnfinishedMessage();
					throw var8;
				} finally {
					if (parsedMessage != null) {
						this.mergeFrom(parsedMessage);
					}

				}

				return this;
			}

			public boolean hasKey() {
				return (this.bitField0_ & 1) == 1;
			}

			public long getKey() {
				return this.key_;
			}

			public Builder setKey(long value) {
				this.bitField0_ |= 1;
				this.key_ = value;
				this.onChanged();
				return this;
			}

			public Builder clearKey() {
				this.bitField0_ &= -2;
				this.key_ = 0L;
				this.onChanged();
				return this;
			}

			public boolean hasValue() {
				return (this.bitField0_ & 2) == 2;
			}

			public ByteString getValue() {
				return this.value_;
			}

			public Builder setValue(ByteString value) {
				if (value == null) {
					throw new NullPointerException();
				} else {
					this.bitField0_ |= 2;
					this.value_ = value;
					this.onChanged();
					return this;
				}
			}

			public Builder clearValue() {
				this.bitField0_ &= -3;
				this.value_ = KStrVPair.getDefaultInstance().getValue();
				this.onChanged();
				return this;
			}
		}
	}

	public interface KStrVPairOrBuilder extends MessageOrBuilder {
		boolean hasKey();

		long getKey();

		boolean hasValue();

		ByteString getValue();
	}

	public static final class KVPair extends GeneratedMessage implements KVPairOrBuilder {
		private static final KVPair defaultInstance = new KVPair(true);
		private final UnknownFieldSet unknownFields;
		public static Parser<KVPair> PARSER = new AbstractParser<KVPair>() {
			public KVPair parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
				return new KVPair(input, extensionRegistry);
			}
		};
		private int bitField0_;
		public static final int KEY_FIELD_NUMBER = 1;
		private long key_;
		public static final int VALUE_FIELD_NUMBER = 2;
		private long value_;
		private byte memoizedIsInitialized;
		private int memoizedSerializedSize;
		private static final long serialVersionUID = 0L;

		private KVPair(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
			super(builder);
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.unknownFields = builder.getUnknownFields();
		}

		private KVPair(boolean noInit) {
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.unknownFields = UnknownFieldSet.getDefaultInstance();
		}

		public static KVPair getDefaultInstance() {
			return defaultInstance;
		}

		public KVPair getDefaultInstanceForType() {
			return defaultInstance;
		}

		public final UnknownFieldSet getUnknownFields() {
			return this.unknownFields;
		}

		private KVPair(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			this.memoizedIsInitialized = -1;
			this.memoizedSerializedSize = -1;
			this.initFields();
			com.google.protobuf.UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();

			try {
				boolean done = false;

				while (!done) {
					int tag = input.readTag();
					switch (tag) {
						case 0:
							done = true;
							break;
						case 8:
							this.bitField0_ |= 1;
							this.key_ = input.readInt64();
							break;
						case 16:
							this.bitField0_ |= 2;
							this.value_ = input.readInt64();
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
				this.unknownFields = unknownFields.build();
				this.makeExtensionsImmutable();
			}

		}

		public static final Descriptor getDescriptor() {
			return CommonProto.internal_static_proto_KVPair_descriptor;
		}

		protected FieldAccessorTable internalGetFieldAccessorTable() {
			return CommonProto.internal_static_proto_KVPair_fieldAccessorTable.ensureFieldAccessorsInitialized(KVPair.class, Builder.class);
		}

		public Parser<KVPair> getParserForType() {
			return PARSER;
		}

		public boolean hasKey() {
			return (this.bitField0_ & 1) == 1;
		}

		public long getKey() {
			return this.key_;
		}

		public boolean hasValue() {
			return (this.bitField0_ & 2) == 2;
		}

		public long getValue() {
			return this.value_;
		}

		private void initFields() {
			this.key_ = 0L;
			this.value_ = 0L;
		}

		public final boolean isInitialized() {
			byte isInitialized = this.memoizedIsInitialized;
			if (isInitialized != -1) {
				return isInitialized == 1;
			} else {
				this.memoizedIsInitialized = 1;
				return true;
			}
		}

		public void writeTo(CodedOutputStream output) throws IOException {
			this.getSerializedSize();
			if ((this.bitField0_ & 1) == 1) {
				output.writeInt64(1, this.key_);
			}

			if ((this.bitField0_ & 2) == 2) {
				output.writeInt64(2, this.value_);
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
					size += CodedOutputStream.computeInt64Size(1, this.key_);
				}

				if ((this.bitField0_ & 2) == 2) {
					size += CodedOutputStream.computeInt64Size(2, this.value_);
				}

				size += this.getUnknownFields().getSerializedSize();
				this.memoizedSerializedSize = size;
				return size;
			}
		}

		protected Object writeReplace() throws ObjectStreamException {
			return super.writeReplace();
		}

		public static KVPair parseFrom(ByteString data) throws InvalidProtocolBufferException {
			return (KVPair) PARSER.parseFrom(data);
		}

		public static KVPair parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			return (KVPair) PARSER.parseFrom(data, extensionRegistry);
		}

		public static KVPair parseFrom(byte[] data) throws InvalidProtocolBufferException {
			return (KVPair) PARSER.parseFrom(data);
		}

		public static KVPair parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
			return (KVPair) PARSER.parseFrom(data, extensionRegistry);
		}

		public static KVPair parseFrom(InputStream input) throws IOException {
			return (KVPair) PARSER.parseFrom(input);
		}

		public static KVPair parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (KVPair) PARSER.parseFrom(input, extensionRegistry);
		}

		public static KVPair parseDelimitedFrom(InputStream input) throws IOException {
			return (KVPair) PARSER.parseDelimitedFrom(input);
		}

		public static KVPair parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (KVPair) PARSER.parseDelimitedFrom(input, extensionRegistry);
		}

		public static KVPair parseFrom(CodedInputStream input) throws IOException {
			return (KVPair) PARSER.parseFrom(input);
		}

		public static KVPair parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
			return (KVPair) PARSER.parseFrom(input, extensionRegistry);
		}

		public static Builder newBuilder() {
			return Builder.create();
		}

		public Builder newBuilderForType() {
			return newBuilder();
		}

		public static Builder newBuilder(KVPair prototype) {
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

		public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements KVPairOrBuilder {
			private int bitField0_;
			private long key_;
			private long value_;

			public static final Descriptor getDescriptor() {
				return CommonProto.internal_static_proto_KVPair_descriptor;
			}

			protected FieldAccessorTable internalGetFieldAccessorTable() {
				return CommonProto.internal_static_proto_KVPair_fieldAccessorTable.ensureFieldAccessorsInitialized(KVPair.class, Builder.class);
			}

			private Builder() {
				this.maybeForceBuilderInitialization();
			}

			private Builder(BuilderParent parent) {
				super(parent);
				this.maybeForceBuilderInitialization();
			}

			private void maybeForceBuilderInitialization() {
				if (KVPair.alwaysUseFieldBuilders) {
				}

			}

			private static Builder create() {
				return new Builder();
			}

			public Builder clear() {
				super.clear();
				this.key_ = 0L;
				this.bitField0_ &= -2;
				this.value_ = 0L;
				this.bitField0_ &= -3;
				return this;
			}

			public Builder clone() {
				return create().mergeFrom(this.buildPartial());
			}

			public Descriptor getDescriptorForType() {
				return CommonProto.internal_static_proto_KVPair_descriptor;
			}

			public KVPair getDefaultInstanceForType() {
				return KVPair.getDefaultInstance();
			}

			public KVPair build() {
				KVPair result = this.buildPartial();
				if (!result.isInitialized()) {
					throw newUninitializedMessageException(result);
				} else {
					return result;
				}
			}

			public KVPair buildPartial() {
				KVPair result = new KVPair(this);
				int from_bitField0_ = this.bitField0_;
				int to_bitField0_ = 0;
				if ((from_bitField0_ & 1) == 1) {
					to_bitField0_ |= 1;
				}

				result.key_ = this.key_;
				if ((from_bitField0_ & 2) == 2) {
					to_bitField0_ |= 2;
				}

				result.value_ = this.value_;
				result.bitField0_ = to_bitField0_;
				this.onBuilt();
				return result;
			}

			public Builder mergeFrom(Message other) {
				if (other instanceof KVPair) {
					return this.mergeFrom((KVPair) other);
				} else {
					super.mergeFrom(other);
					return this;
				}
			}

			public Builder mergeFrom(KVPair other) {
				if (other == KVPair.getDefaultInstance()) {
					return this;
				} else {
					if (other.hasKey()) {
						this.setKey(other.getKey());
					}

					if (other.hasValue()) {
						this.setValue(other.getValue());
					}

					this.mergeUnknownFields(other.getUnknownFields());
					return this;
				}
			}

			public final boolean isInitialized() {
				return true;
			}

			public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
				KVPair parsedMessage = null;

				try {
					parsedMessage = (KVPair) KVPair.PARSER.parsePartialFrom(input, extensionRegistry);
				} catch (InvalidProtocolBufferException var8) {
					parsedMessage = (KVPair) var8.getUnfinishedMessage();
					throw var8;
				} finally {
					if (parsedMessage != null) {
						this.mergeFrom(parsedMessage);
					}

				}

				return this;
			}

			public boolean hasKey() {
				return (this.bitField0_ & 1) == 1;
			}

			public long getKey() {
				return this.key_;
			}

			public Builder setKey(long value) {
				this.bitField0_ |= 1;
				this.key_ = value;
				this.onChanged();
				return this;
			}

			public Builder clearKey() {
				this.bitField0_ &= -2;
				this.key_ = 0L;
				this.onChanged();
				return this;
			}

			public boolean hasValue() {
				return (this.bitField0_ & 2) == 2;
			}

			public long getValue() {
				return this.value_;
			}

			public Builder setValue(long value) {
				this.bitField0_ |= 2;
				this.value_ = value;
				this.onChanged();
				return this;
			}

			public Builder clearValue() {
				this.bitField0_ &= -3;
				this.value_ = 0L;
				this.onChanged();
				return this;
			}
		}
	}

	public interface KVPairOrBuilder extends MessageOrBuilder {
		boolean hasKey();

		long getKey();

		boolean hasValue();

		long getValue();
	}
}
