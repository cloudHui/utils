package net.message;

public class TCPMessage {
	private int result;
	private int messageId;
	private int clientId;
	private int mapId;
	private long sequence;
	private byte[] message;

	public static TCPMessage newInstance(int messageId, byte[] message, long sequence) {
		return new TCPMessage(messageId, message, sequence);
	}

	public static TCPMessage newInstance(int result, int messageId, int clientId, byte[] message) {
		return new TCPMessage(result, messageId, clientId, message);
	}

	public static TCPMessage newInstance(int result, int messageId, int clientId, byte[] message, int mapId) {
		return new TCPMessage(result, messageId, clientId, message, mapId);
	}

	public static TCPMessage newInstance(int result, int messageId, int clientId, byte[] message, long sequence) {
		return new TCPMessage(result, messageId, clientId, message, sequence);
	}

	public static TCPMessage newInstance(int result, int messageId, int clientId, byte[] message, int mapId, long sequence) {
		return new TCPMessage(result, messageId, clientId, message, mapId, sequence);
	}

	public TCPMessage(int messageId, byte[] message, long sequence) {
		this.messageId = messageId;
		this.message = message;
		this.sequence = sequence;
	}

	public TCPMessage(int result, int messageId, int clientId, byte[] message, int mapId) {
		this.result = result;
		this.messageId = messageId;
		this.clientId = clientId;
		this.message = message;
		this.mapId = mapId;
	}

	public TCPMessage(int result, int messageId, int clientId, byte[] message) {
		this.result = result;
		this.messageId = messageId;
		this.clientId = clientId;
		this.message = message;
	}

	public TCPMessage(int result, int messageId, int clientId, byte[] message, long sequence) {
		this.result = result;
		this.messageId = messageId;
		this.clientId = clientId;
		this.message = message;
		this.sequence = sequence;
	}

	public TCPMessage(int result, int messageId, int clientId, byte[] message, int mapId, long sequence) {
		this.result = result;
		this.messageId = messageId;
		this.clientId = clientId;
		this.message = message;
		this.mapId = mapId;
		this.sequence = sequence;
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

	public int getClientId() {
		return this.clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
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

	public long getSequence() {
		return sequence;
	}

	public void setSequence(long sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		return "TCPMessage{" +
				"messageId=" + messageId +
				", sequence=" + sequence +
				'}';
	}
}
