package com.legend.factory;

import com.legend.utils.scan.ScanUtil;
import com.legend.xml.XmlApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * @author Legend
 * @data by on 18-10-7.
 * @description 提供需要注入到容器中的类
 */
public class BeanDefinition extends XmlApplicationContext {

    public List<String> getComponentList(String contextConfigLocation) {
        List<String> componentList = super.getComponentList(contextConfigLocation);
        return componentList;
    }

    public Map<String, GenericBeanDefinition> getBeanDefinitionXmlMap(String contextConfigLocation) {
        Map<String, GenericBeanDefinition> beanDefinitionMap = super.getBeanDefinitionMap(contextConfigLocation);
        return beanDefinitionMap;
    }
}
