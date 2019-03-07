package com.zjs.bwcx.threadnotsafe;

public class Count {
	public int num = 0;
	
	public /*synchronized*/ void add() {
		try {
			Thread.sleep(51);//模仿用户干活
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		num += 1;
		System.out.println(Thread.currentThread().getName()+"-"+num);
	}
}
