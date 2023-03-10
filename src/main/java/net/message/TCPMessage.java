package net.message;

public class TCPMessage {
	private int version;
	private int messageId;
	private int sequence;
	private int mapId;
	private byte[] message;

	public static TCPMessage newInstance(int version, int messageId, int sequence, byte[] message) {
		return new TCPMessage(version, messageId, sequence, message);
	}

	public static TCPMessage newInstance(int version, int messageId, int sequence, byte[] message, int mapId) {
		return new TCPMessage(version, messageId, sequence, message,mapId);
	}

	public TCPMessage(int version, int messageId, int sequence, byte[] message, int mapId) {
		this.version = version;
		this.messageId = messageId;
		this.sequence = sequence;
		this.message = message;
		this.mapId = mapId;
	}

	public TCPMessage(int version, int messageId, int sequence, byte[] message) {
		this.version = version;
		this.messageId = messageId;
		this.sequence = sequence;
		this.message = message;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getMessageId() {
		return this.messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getSequence() {
		return this.sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public byte[] getMessage() {
		return this.message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
}
