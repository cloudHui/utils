package net.connect.handle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConnectManager {
	private final AtomicLong ID;
	private final Map<Long, ConnectHandler> connectMap;
	private final ReadWriteLock lock;

	private ConnectManager() {
		ID = new AtomicLong(0L);
		connectMap = new HashMap<>(64);
		lock = new ReentrantReadWriteLock(false);
	}

	public static ConnectManager getInstance() {
		return new ConnectManager();
	}

	public long getId() {
		return ID.incrementAndGet();
	}

	public void addConnect(ConnectHandler connect) {
		lock.writeLock().lock();
		try {
			connectMap.put(connect.getId(), connect);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void removeConnect(long id) {
		lock.writeLock().lock();
		try {
			connectMap.remove(id);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public ConnectHandler getConnect(long id) {
		lock.readLock().lock();
		ConnectHandler handler;
		try {
			handler = connectMap.get(id);
		} finally {
			lock.readLock().unlock();
		}
		return handler;
	}
}