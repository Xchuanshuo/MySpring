package com.legend.utils;

/**
 * @author Legend
 * @data by on 18-10-7.
 * @description
 */
public class StringUtils {

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length()==0) {
            return true;
        }
        return false;
    }
}
