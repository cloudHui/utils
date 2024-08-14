package net.message;

public class TCPMessage {
	private int result;
	private int messageId;
	private int roleId;
	private int mapId;
	private byte[] message;

	public static TCPMessage newInstance(int result, int messageId, int roleId, byte[] message) {
		return new TCPMessage(result, messageId, roleId, message);
	}

	public static TCPMessage newInstance(int result, int messageId, int roleId, byte[] message, int mapId) {
		return new TCPMessage(result, messageId, roleId, message,mapId);
	}

	public TCPMessage(int result, int messageId, int roleId, byte[] message, int mapId) {
		this.result = result;
		this.messageId = messageId;
		this.roleId = roleId;
		this.message = message;
		this.mapId = mapId;
	}

	public TCPMessage(int result, int messageId, int roleId, byte[] message) {
		this.result = result;
		this.messageId = messageId;
		this.roleId = roleId;
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

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
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
