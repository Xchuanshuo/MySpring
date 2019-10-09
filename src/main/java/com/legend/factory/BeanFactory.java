package com.legend.factory;

/**
 * @author Legend
 * @data by on 18-10-7.
 * @description
 */
public interface BeanFactory {

    /**
     * 获取bean
     * @param beanId
     * @return
     */
    Object getBean(String beanId);
}
