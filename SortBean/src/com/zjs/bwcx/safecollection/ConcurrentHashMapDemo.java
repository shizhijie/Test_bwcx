package com.zjs.bwcx.safecollection;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapDemo {
	
	public static void main(String[] args) {
		ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
		map.put("one", 1);
		map.put("two", 2);
		map.put("three", 3);
		System.out.println(map.get("two"));
		if (map.containsKey("one")&&map.get("one").equals(1)) {
			map.remove("one");
		}
	}
	
}
