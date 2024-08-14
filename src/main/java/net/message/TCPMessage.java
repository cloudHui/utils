package net.message;

public class TCPMessage {
	private int result;
	private int messageId;
	private int sequence;
	private int mapId;
	private byte[] message;

	public static TCPMessage newInstance(int result, int messageId, int sequence, byte[] message) {
		return new TCPMessage(result, messageId, sequence, message);
	}

	public static TCPMessage newInstance(int result, int messageId, int sequence, byte[] message, int mapId) {
		return new TCPMessage(result, messageId, sequence, message,mapId);
	}

	public TCPMessage(int result, int messageId, int sequence, byte[] message, int mapId) {
		this.result = result;
		this.messageId = messageId;
		this.sequence = sequence;
		this.message = message;
		this.mapId = mapId;
	}

	public TCPMessage(int result, int messageId, int sequence, byte[] message) {
		this.result = result;
		this.messageId = messageId;
		this.sequence = sequence;
		this.message = message;
	}

	public int getResult() {
		return this.result;
	}

	public void setResult(int result) {
		this.result = result;
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
