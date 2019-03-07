package com.zjs.bwcx.threadnotsafe;

public class ThreadA extends Thread{
	
	private Count count;
	
	public ThreadA(Count count) {
		this.count = count;
	}

	@Override
	public void run() {
		count.add();
	}
}
