package net.client.handler;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.client.Sender;

public class SenderManager {
	public static final SenderManager INSTANCE = new SenderManager();
	private int id = 1;
	private final int INIT_SIZE = 4096;
	public final Map<Integer, Sender> clientMap = new HashMap<>(INIT_SIZE);
	private final ReadWriteLock lock;
	private final Set<Integer> clientIds = new LinkedHashSet<>(INIT_SIZE);

	private SenderManager() {
		lock = new ReentrantReadWriteLock();
	}

	/**
	 * 获取id 要么从废旧ID池子 直接拿 要么就再生成 有个隐患 除非同时在线 超过了 Integer.MAX_VALUE 就有几率重复然后客户端错误
	 *
	 * @return 获取链接自增id
	 */
	public int getId() {
		lock.writeLock().lock();
		try {
			int outId = 0;
			if (!clientIds.isEmpty()) {
				for (Integer id : clientIds) {
					outId = id;
					break;
				}
			}

			if (outId > 0) {
				clientIds.remove(outId);
			} else {
				if (id < Integer.MAX_VALUE) {
					outId = ++id;
				} else {
					outId = id = 1;
				}
			}
			return outId;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void addClient(Sender client) {
		lock.writeLock().lock();
		try {
			if(client instanceof ClientHandler){
				clientMap.put(((ClientHandler) client).getId(), client);
			} else if(client instanceof WsClientHandler){
				clientMap.put(((WsClientHandler) client).getId(), client);
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void removeClient(Sender client) {
		lock.writeLock().lock();
		try {
			if(client instanceof ClientHandler){
				removeClient(((ClientHandler) client).getId());
			} else if(client instanceof WsClientHandler){
				removeClient(((WsClientHandler) client).getId());
			}

		} finally {
			lock.writeLock().unlock();
		}
	}

	public void removeClient(int id) {
		lock.writeLock().lock();
		try {
			clientMap.remove(id);
			clientIds.add(id);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public Sender getClient(int id) {
		lock.writeLock().lock();
		try {
			return clientMap.get(id);
		} finally {
			lock.writeLock().unlock();
		}
	}
}