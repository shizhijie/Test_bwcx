package com.zjs.bwcx.deadlock;

public class ThreadA extends Thread{
	
	private Count count;

	public ThreadA(Count count) {
		super();
		this.count = count;
	}


	@Override
	public void run() {
		count.add();
	}
	
	
}
