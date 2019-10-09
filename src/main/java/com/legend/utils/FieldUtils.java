package com.legend.utils;

/**
 * @author Legend
 * @data by on 18-10-7.
 * @description 字段处理工具类
 */
public class FieldUtils {

    /**
     * 根据字段名拼接Set方法
     * @param propertyName
     * @return
     */
    public static String getSetMethodNameByNameField(String propertyName) {
        String methodName = "set"+propertyName.substring(0, 1).toUpperCase()+propertyName.substring(1);
        return methodName;
    }

    /**
     * 根据字段名拼接Get方法
     * @param propertyName
     * @return
     */
    public static String getGetMethodNameByNameField(String propertyName) {
        String methodName = "get"+propertyName.substring(0, 1).toUpperCase()+propertyName.substring(1);
        return methodName;
    }
}
