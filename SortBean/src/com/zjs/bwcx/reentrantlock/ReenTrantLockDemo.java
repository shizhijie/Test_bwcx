package com.zjs.bwcx.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

public class ReenTrantLockDemo {
	
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
	final ReentrantLock lock = new ReentrantLock();
	public void get() {
		//final ReentrantLock lock = new ReentrantLock();
		try {
			lock.lock();//加锁
			System.out.println(Thread.currentThread().getName()+"get begin");
			Thread .sleep(1000L);//模仿干活
			System.out.println(Thread.currentThread().getName()+"get end");
			lock.unlock();//解锁
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void put() {
		//final ReentrantLock lock = new ReentrantLock();
		try {
			lock.lock();//加锁
			System.out.println(Thread.currentThread().getName()+"put begin");
			Thread.sleep(1000L);//模仿干活
			System.out.println(Thread.currentThread().getName()+"put end");
			lock.unlock();//解锁
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
