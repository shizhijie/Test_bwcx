package com.zjs.bwcx.deadlock;

public class TestDeadLock {
	
	public static void main(String[] args) {
		final Count count = new Count();
		ThreadA tA = new ThreadA(count);
		tA.setName("线程A");
		tA.start();
		ThreadB tB = new ThreadB(count);
		tB.setName("线程B");
		tB.start();
	}
}
