package com.zjs.bwcx.displacement;

public class Test {

	public static void main(String[] args) {
		int number = 10;
		// 原始数二进制
		printInfo(number);
		number = number << 1;
		System.out.println(number);
		// 左移一位
		printInfo(number);
		number = number >>> 1;
		// 右移一位
		printInfo(number);
		System.out.println(number);
	}

	/**
	 * 输出一个int的二进制数
	 * 
	 * @param num
	 */
	private static void printInfo(int num) {
		System.out.println(Integer.toBinaryString(num));
	}

}
