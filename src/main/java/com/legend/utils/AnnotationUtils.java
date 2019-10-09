package com.legend.utils;

/**
 * @author Legend
 * @data by on 18-10-7.
 * @description 注解处理工具类
 */
public class AnnotationUtils {

    public static <T> boolean isEmpty(T t) {
        return t == null? true: false;
    }
}
