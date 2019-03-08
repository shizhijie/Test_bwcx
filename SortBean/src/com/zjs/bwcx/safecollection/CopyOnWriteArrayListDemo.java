package com.zjs.bwcx.safecollection;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListDemo {
	
	public static void main(String[] args) {
		CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
		list.add("one");
		list.add("two");
		list.add("three");//list下标是从0开始的
		System.out.println(list.get(1));
		if (list.contains("three")) {
			Iterator<String> iterator = list.iterator();
			while (iterator.hasNext()) {
				System.out.println(iterator.next());
			}
		}
	}
}
