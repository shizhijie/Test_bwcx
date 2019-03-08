package com.zjs.bwcx.deadlock;

public class ThreadB extends Thread{
	
	private Count count;

	public ThreadB(Count count) {
		this.count = count;
	}
	@Override
	public void run() {
		count.lockMethod();
	}
	
	
}
