package com.legend.utils;

/**
 * @author Legend
 * @data by on 18-10-7.
 * @description 类型处理工具类
 */
public class TypeUtils {

    /**
     * 判断是不是基本类型
     * @param typeName
     * @return
     */
    public static boolean isBasicType(String typeName){
        if (typeName.equals("String")){
            return true;
        } else if(typeName.equals("Integer")){
            return true;
        } else if(typeName.equals("int")){
            return true;
        } else if(typeName.equals("Long")){
            return true;
        } else if(typeName.equals("Short")){
            return true;
        } else if(typeName.equals("Float")){
            return true;
        } else if(typeName.equals("Double")){
            return true;
        } else if(typeName.equals("Byte")){
            return true;
        }
        return false;
    }

    /**
     * 根据传入的属性和类名 将属性名强转为类名的属性
     * @param className
     * @param parameter
     * @return
     */
    public static Object convert(String className, String parameter) {
        if (className.equals("String")){
            return parameter;
        } else if (className.equals("Integer")){
            return Integer.valueOf(parameter);
        } else if (className.equals("int")){
            return Integer.valueOf(parameter);
        } else if (className.equals("Float")){
            return Float.valueOf(parameter);
        } else if (className.equals("Double")){
            return Integer.valueOf(parameter);
        } else if (className.equals("Long")){
            return Long.valueOf(parameter);
        } else if (className.equals("Short")){
            return Short.valueOf(parameter);
        } else if (className.equals("Byte")){
            return Byte.valueOf(parameter);
        } else if (className.equals("Boolean")){
            return Boolean.valueOf(parameter);
        }
        return null;
    }
}
