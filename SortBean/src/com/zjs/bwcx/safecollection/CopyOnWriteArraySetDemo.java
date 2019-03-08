package com.zjs.bwcx.safecollection;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

public class CopyOnWriteArraySetDemo {
	
	public static void main(String[] args) {
		java.util.concurrent.CopyOnWriteArraySet<String> set = new CopyOnWriteArraySet<>();
		set.add("one");
		set.add("three");
		if (set.contains("three")) {
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				System.out.println(iterator.next());
			}
		}
	}
}
