package com.zjs.bwcx.readwritelock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {
	
	public static void main(String[] args) {
		
		final Count count = new Count();
		for (int i = 0; i < 2; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					count.get();
				}
			}).start();
		}
		for (int i = 0; i < 2; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					count.put();
				}
			}).start();
		}
		
	}
	
}
class Count{
	private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	public void get() {
		rwl.readLock().lock();//上读锁，其他线程只能读不能写，具有高并发
		try {
			System.out.println(Thread.currentThread().getName() +" read start.");
			Thread.sleep(1000L);//模拟干活
			System.out.println(Thread.currentThread().getName() +" read end.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			rwl.readLock().unlock();//释放读锁 ，最好放在finally里面
		}
	}
	public void put() {
		rwl.writeLock().lock();//上写锁，具有阻塞性
		try {
			System.out.println(Thread.currentThread().getName()+ "write start.");
			Thread.sleep(1000L);//模拟干活
			System.out.println(Thread.currentThread().getName()+"write end.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			rwl.writeLock().unlock();//释放写锁，再好放在finally里面
		}
	}
}
