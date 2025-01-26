package net.connect;

/**
 * 服务信息
 */
public class ServerInfo {
	private int serverType;

	private int serverId;

	private String ipConfig;

	public ServerInfo(int serverType, int serverId, String ipConfig) {
		this.serverType = serverType;
		this.serverId = serverId;
		this.ipConfig = ipConfig;
	}

	public int getServerType() {
		return serverType;
	}

	public void setServerType(int serverType) {
		this.serverType = serverType;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getIpConfig() {
		return ipConfig;
	}

	public void setIpConfig(String ipConfig) {
		this.ipConfig = ipConfig;
	}

	@Override
	public String toString() {
		return "ServerInfo{" + "serverType=" + serverType + ", serverId=" + serverId + ", ipConfig='" + ipConfig + '\'' + '}';
	}
}
