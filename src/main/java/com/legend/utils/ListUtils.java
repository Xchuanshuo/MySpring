package com.legend.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Legend
 * @data by on 18-10-7.
 * @description
 */
public class ListUtils {

    /**
     * 添加数据 保证只有一个相同实例
     * @param list
     * @param t
     * @param <T>
     */
    public static <T> void add(List<T> list, T t) {
        Set<T> set = new HashSet<>(list);
        if (set.add(t)) list.add(t);
    }

    public static boolean isEmpty(List list) {
        if (list==null || list.size()==0) {
            return true;
        }
        return false;
    }
}
