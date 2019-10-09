package com.legend.factory;

import lombok.Data;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-10-7.
 * @description 存放bean的子元素的容器
 */
@Data
public class GenericBeanDefinition {

    /**
     *  className和xml中的class对应
     */
    private String className;

    /**
     * bean下面的属性集合
     */
    private List<ChildBeanDefinition> childBeanDefinitionList;
}
