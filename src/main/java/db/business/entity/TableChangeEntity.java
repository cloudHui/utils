package db.business.entity;

import java.util.Date;

public class TableChangeEntity {
	private String tableName;
	private int gameId;
	private int channelId;
	private Date updateTime;

	public TableChangeEntity() {
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getGameId() {
		return this.gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getChannelId() {
		return this.channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String toString() {
		return "TableChangeEntity{tableName='" + this.tableName + '\'' + ", gameId=" + this.gameId + ", channelId=" + this.channelId + ", updateTime=" + this.updateTime + '}';
	}
}
