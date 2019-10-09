package com.legend.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Legend
 * @data by on 18-10-28.
 * @description
 */
public class ReflectionUtil {

    public static Object newInstance(Class<?> cls) {
        Object instance = null;
        try {
            instance = cls.getConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public static Object newInstance(String className) {
        Class<?> clazz = ClassUtils.loadClass(className);
        return newInstance(clazz);
    }

    // 调用方法
    public static Object invokeMethod(Object obj, Method method, Object...args) {
        Object object = null;
        method.setAccessible(true);
        try {
            object = method.invoke(obj, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    // 设置成员变量的值
    public static void setField(Object obj, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
