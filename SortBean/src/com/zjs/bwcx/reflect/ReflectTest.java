package com.zjs.bwcx.reflect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReflectTest {
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		Class clazz = Student.class;
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			Object obj = clazz.newInstance();
			Field[] declaredFields = clazz.getDeclaredFields();
			for(Field field:declaredFields) {
				field.setAccessible(true);//设置可以访问私有属性
				if (field.getName().equals("id")) {
					field.set(obj, UUID.randomUUID()+"---"+i);
				}
				if (field.getName().equals("name")) {
					field.set(obj, "name"+i);
				}
				if (field.getName().equals("age")) {
					field.set(obj, 20+i);
				}
			}
			list.add(obj);
		}
		System.out.println(list);
	}
}
