package com.zjs.bwcx.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class TestAtomic {
	
	public static void main(String[] args) {
		AtomicInteger aInteger = new AtomicInteger(0);
		//获取当前值0
		System.out.println(aInteger.get());
		//获取当前值，并设置新的值0
		System.out.println(aInteger.getAndSet(5));
		//获取当前的值，并自增5
		System.out.println(aInteger.getAndIncrement());
		//获取当前的值，并自减6
		System.out.println(aInteger.getAndDecrement());
		//获取当前的值，并加上预期的值5
		System.out.println(aInteger.getAndAdd(10));
		//获取当前的值15
		System.out.println(aInteger.get());
		
	}
}
