package com.zjs.bwcx.safecollection;

import java.util.Hashtable;

public class HashTableDemo {
	
	public static void main(String[] args) {
		Hashtable<String, Integer> numbers = new Hashtable<>();
		numbers.put("one", 1);
		numbers.put("two", 2);
		numbers.put("three", 3);
		Integer n = numbers.get("two");
		if (n!=null) {
			System.out.println("two="+n);
		}
	}
}
