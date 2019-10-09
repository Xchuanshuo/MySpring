package com.legend.mybatis;

import java.lang.reflect.Proxy;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description
 */
public class MySqlSession {

    public Object select(MapperInfo mapperInfo, Object[] parameters) {
        Executor executor = new Executor();
        return executor.query(mapperInfo, parameters);
    }

    public Integer update(MapperInfo mapperInfo, Object[] parameters) {
        Executor executor = new Executor();
        return executor.update(mapperInfo, parameters);
    }

    public <T> T getMapper(Class<?> clazz, String xmlName) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new MapperProxy(this, xmlName));
    }
}
