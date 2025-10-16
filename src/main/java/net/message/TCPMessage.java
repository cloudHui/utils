package net.message;

public class TCPMessage {
	private int result;
	private int messageId;
	private int clientId;
	private int mapId;
	private int sequence;
	private byte[] message = null;

	public static TCPMessage newInstance(int result) {
		return new TCPMessage(result);
	}

	public static TCPMessage newInstance(int messageId, byte[] message, int sequence) {
		return new TCPMessage(messageId, message, sequence);
	}

	public static TCPMessage newInstance(int result, int messageId, int clientId, byte[] message) {
		return new TCPMessage(result, messageId, clientId, message);
	}

	public static TCPMessage newInstance(int result, int messageId, int clientId, byte[] message, int sequence) {
		return new TCPMessage(result, messageId, clientId, message, sequence);
	}

	public static TCPMessage newInstance(int result, int messageId, int clientId, byte[] message, int mapId, int sequence) {
		return new TCPMessage(result, messageId, clientId, message, mapId, sequence);
	}

	public static TCPMessage newInstance(int messageId, int clientId, byte[] message, int mapId, int sequence) {
		return new TCPMessage(0,messageId, clientId, message, mapId, sequence);
	}

	public TCPMessage(int result) {
		this.result = result;
	}

	public TCPMessage(int messageId, byte[] message, int sequence) {
		this.messageId = messageId;
		this.message = message;
		this.sequence = sequence;
	}

	public TCPMessage(int result, int messageId, int clientId, byte[] message) {
		this.result = result;
		this.messageId = messageId;
		this.clientId = clientId;
		this.message = message;
	}

	public TCPMessage(int result, int messageId, int clientId, byte[] message, int sequence) {
		this.result = result;
		this.messageId = messageId;
		this.clientId = clientId;
		this.message = message;
		this.sequence = sequence;
	}

	public TCPMessage(int result, int messageId, int clientId, byte[] message, int mapId, int sequence) {
		this.result = result;
		this.messageId = messageId;
		this.clientId = clientId;
		this.message = message;
		this.mapId = mapId;
		this.sequence = sequence;
	}


	public TCPMessage(int messageId, int clientId, byte[] message, int mapId, int sequence) {
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

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		return "TCPMessage{" +
				"result=" + result +
				", messageId=" + messageId +
				", sequence=" + sequence +
				'}';
	}
}
