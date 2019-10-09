package com.legend.xml.rule;

import lombok.Getter;

/**
 * @author Legend
 * @data by on 18-10-7.
 * @description Ioc中xml配置的规则
 */
@Getter
public enum IocRules {
    /**
     * bean配置的规则
     */
    BEAN_RULE("bean", "id", "class"),
    /**
     * 要扫描的组件配置的规则
     */
    SCAN_RULE("component-scan", "base-package", "null"),
    /**
     * set注入的规则
     */
    SET_INJECT("property", "name", "value"),
    /**
     * 构造器注入规则
     */
    CONS_INJECT("constructor-arg", "value", "index");

    private String type;
    private String name;
    private String value;
    IocRules(String type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }
}
