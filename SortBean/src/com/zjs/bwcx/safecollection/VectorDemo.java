package com.zjs.bwcx.safecollection;

import java.util.Iterator;
import java.util.Vector;

public class VectorDemo {
	public static void main(String[] args) {
		Vector<String> list = new Vector<>();
		list.add("one");
		list.add("two");
		list.add("three");
		list.remove("two");
		if (list.contains("three")) {
			Iterator<String> iterator = list.iterator();
			while (iterator.hasNext()) {
				System.out.println(iterator.next());
			}
		}
	}
}
