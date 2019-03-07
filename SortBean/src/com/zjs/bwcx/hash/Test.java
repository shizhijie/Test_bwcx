package com.zjs.bwcx.hash;

public class Test {
	
	public static void main(String[] args) {
		
		String aString = "a";
		System.out.println(aString.hashCode());
		System.out.println(new Integer(2).hashCode());
		System.out.println(new Object().hashCode());
		System.out.println(new Object().hashCode());
	}
	
}
