package com.legend.xml;

import com.legend.factory.GenericBeanDefinition;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author Legend
 * @data by on 18-10-7.
 * @description
 */
@Slf4j
public class FileSystemXmlApplicationContext extends XmlApplicationContext {

    public Map<String, GenericBeanDefinition> getGenericBeanDefinition(String contextConfigLocation) {
        Map<String, GenericBeanDefinition> beanDefinitionMap = super.getBeanDefinitionMap(contextConfigLocation);
        return beanDefinitionMap;
    }
}
