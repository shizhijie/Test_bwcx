package com.zjs.bwcx.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class UseCountDownLatch {
	
	public static void main(String[] args) throws InterruptedException {
		
		final CountDownLatch cLatch = new CountDownLatch(1);
		final CountDownLatch cLatch2 = new CountDownLatch(1);
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					cLatch2.countDown();
					cLatch.await();
					System.out.println(Thread.currentThread().getName()+"执行");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		t1.start();
		cLatch2.await();
		cLatch.countDown();
		System.out.println(Thread.currentThread().getName()+"执行");
		System.out.println("2111");
	}
}
