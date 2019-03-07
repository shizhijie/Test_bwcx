package com.zjs.bwcx;

public class Test {
	private static void Print() {
		System.out.println("Print()");
	}

	public static void main(String[] args) {
		System.out.println(Runtime.getRuntime().availableProcessors());
		((Test) null).Print();

	}
}
