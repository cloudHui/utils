package net.message;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

public interface Parser {
	Message parser(int var1, byte[] var2) throws InvalidProtocolBufferException;
}
