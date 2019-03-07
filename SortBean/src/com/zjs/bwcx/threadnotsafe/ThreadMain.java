package com.zjs.bwcx.threadnotsafe;

public class ThreadMain {
	
	public static void main(String[] args) {
		
		Count count = new Count();
		for (int i = 0; i < 5; i++) {
			ThreadA threadA = new ThreadA(count);
			threadA.start();
		}
		try {
			Thread.sleep(1001);//等待5个人干完活
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("5个人干完活最后的值是"+count.num);
	}
	
}
