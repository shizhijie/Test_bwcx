package com.zjs.bwcx.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestPerson {

	public static void main(String[] args) {
		Person p1 = new Person("张三", 25);
		Person p2 = new Person("李四", 15);
		Person p3 = new Person("王五", 32);
		Person p4 = new Person("赵六", 28);
		Person p5 = new Person("赵六", 80);
		Person p6 = new Person("李四", 15);

		List<Person> list = new ArrayList<Person>();
		list.add(p1);
		list.add(p2);
		list.add(p3);
		list.add(p4);
		list.add(p5);
		list.add(p6);
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
}
