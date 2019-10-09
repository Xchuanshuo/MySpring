package com.legend.utils;

import com.alibaba.fastjson.JSON;
import com.legend.demo.model.User;

/**
 * @author Legend
 * @data by on 18-10-13.
 * @description
 */
public class JsonUtils {

    public static String objToJson(Object object) {
        return JSON.toJSONString(object);
    }

    public static <T> T jsonToObj(String string, Class<T> clazz) {
        return JSON.parseObject(string, clazz);
    }

    public static void main(String[] args) {
        User user = new User();
        user.setUid(10);
        user.setUsername("Legend");
        user.setPassword("1111111");
        String jsonStr = objToJson(user);
        System.out.println(jsonStr);
        User obj = jsonToObj(jsonStr, User.class);
        System.out.println(obj.getUsername());
    }
}
