package com.zjs.bwcx.readwritelock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo2 {
	
	public static void main(String[] args) {
		
		final Count1 count = new Count1();
		for (int i = 0; i < 5; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Object readWrite = count.readWrite("zjs1");
					System.out.println(Thread.currentThread().getName()+" result is:"+readWrite);
				}
			}).start();
		}
		
	}
	
}
class Count1{
	private static final Map<String, Object> map = new HashMap<>();
	static {
		map.put("zjs", "aaa");
		System.out.println(map.get("zjs"));
	}
	private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	public Object readWrite(String id) {
		Object value = null;
		rwl.readLock().lock();//首先开启读锁，从缓存中去取
		try {
			value = map.get(id);
			if (value==null) {//如果缓存中没有释放读锁，上写锁
				rwl.readLock().unlock();
				rwl.writeLock().lock();
				try {
					if (value==null) {
						value = "aa";//此时可以去数据库中查找，这里简单模拟一下
					}
				} finally {
					rwl.writeLock().unlock();//释放写锁
				}
				rwl.readLock().lock();//然后在上读锁
			}
		} finally {
			rwl.readLock().unlock();//最后释放读锁
		}
		return value;
	}
}
