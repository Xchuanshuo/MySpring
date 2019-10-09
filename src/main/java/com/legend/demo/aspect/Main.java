package com.legend.demo.aspect;

import com.legend.factory.InitBean;

import java.util.Map;

/**
 * @author Legend
 * @data by on 18-10-28.
 * @description
 */
public class Main {

    public static void main(String[] args) {
        InitBean initBean = new InitBean();
        initBean.initBeans();
        Map<String, Object> beanContainerMap = initBean.beanContainerMap;
        Test test = (Test) beanContainerMap.get("Test");
        test.doSomething();
        System.out.println("----------------------------------");
        test.doSomethingWithNotProxy();
    }
}
