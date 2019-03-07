package com.zjs.bwcx.reflect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Student {
	
	private String id;
	private String name;
	private int age;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
	
	static class Reflect{
		public static void main(String[] args) throws InstantiationException, IllegalAccessException {
			Class<Student> clazz = Student.class;
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
}