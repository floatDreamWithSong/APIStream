package com.daydreamer.faastest.demo;

import java.lang.reflect.Field;

public class ObjectPrinter {

    public static void printFields(Object obj) {
        Class<?> objClass = obj.getClass();
        System.out.println("Fields of " + objClass.getName() + ":");

        // 遍历类及其所有父类，直到Object类
        while (objClass != null) {
            Field[] fields = objClass.getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true); // 确保私有字段也可以访问
                    String fieldName = field.getName();
                    Object fieldValue = field.get(obj);
                    System.out.println(fieldName + ": " + fieldValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            objClass = objClass.getSuperclass(); // 移动到父类
        }
    }

    public static void main(String[] args) {
        Object obj = new TestClass();
        printFields(obj);
    }
}

class TestClass {
    String name = "test";
    TestClass2 obj = new TestClass2();
}
class TestClass2 {
    int age = 0;
}