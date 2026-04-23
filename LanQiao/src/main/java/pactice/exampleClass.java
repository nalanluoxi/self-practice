package pactice;

import example.java2.Student;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLOutput;

public class exampleClass {
    public static void main(String[] args) throws NoSuchMethodException, Exception, IllegalAccessException, InvocationTargetException {
        Class c= Student.class;
        Method[] methods = c.getMethods();
        Method[] declaredMethods = c.getDeclaredMethods();
        Method method1 = c.getMethod("setAge", int.class);
        Method method2 = c.getDeclaredMethod("setName", String.class);
        Student stu=new Student();
        method2.invoke(stu,"wangwu");
        System.out.println(stu.getName());

    }

}
