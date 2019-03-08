package com.zjs.bwcx.single;

import java.util.concurrent.locks.ReentrantLock;

public class Singleton {//线程不安全，不正确的写法
	
	private static Singleton instance;
	private Singleton() {
	}
	public static Singleton getInstance() {
		if (instance==null) {
			instance = new Singleton();
		}
		return instance;
	}
}
class Singleton2{//线程安全性，但是高并发性能不是很高的写法
	private static Singleton2 instance;
	private Singleton2() {}
	public static synchronized Singleton2 getInstance2() {
		if (instance==null) {
			instance = new Singleton2();
		}
		return instance;
	}
}

class Singleton3{//线程安全，性能又高的，这种方法最为常见
	private static Singleton3 instance;
	private static byte[] lock = new byte[0];
	private Singleton3() {}
	public static Singleton3 getInstance() {
		if (instance==null) {
			synchronized (lock) {
				if (instance==null) {
					instance = new Singleton3();
				}
			}
		}
		return instance;
	}
}

class Singleton4{
	private static Singleton4 instance;
	private static ReentrantLock lock = new ReentrantLock();
	private Singleton4() {}
	public static Singleton4 getInstance() {
		if (instance==null) {
			lock.lock();
			if (instance==null) {
				instance = new Singleton4();
			}
			lock.unlock();
		}
		return instance;
	}
}
