package com.legend.xml;

import com.legend.constants.Constants;
import com.legend.exception.XmlException;
import com.legend.factory.ChildBeanDefinition;
import com.legend.factory.GenericBeanDefinition;
import com.legend.utils.ListUtils;
import com.legend.utils.StringUtils;
import com.legend.utils.scan.ScanUtil;
import com.legend.xml.rule.IocRules;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.legend.xml.rule.IocRules.*;

/**
 * @author Legend
 * @data by on 18-10-7.
 * @description 封装xml解析 仿IOC
 */
@Slf4j
public class XmlApplicationContext {

    /**
     * 将xml中的bean元素注入到容器
     * @param contextConfigLocation
     * @return 指定xml中的bean容器
     */
    public Map<String, GenericBeanDefinition> getBeanDefinitionMap(String contextConfigLocation) {
        Map<String, GenericBeanDefinition> beanDefinitionXmlMap = new ConcurrentHashMap<>();
        List<Element> elementList = getElements(contextConfigLocation);
        // 遍历每一个bean 注入beanDefinitionMap
        for (Element element: elementList) {
            if (element.getName().equals("bean")) {
                GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
                // bean下面的子元素列表
                List<ChildBeanDefinition> childBeanDefinitionList = new ArrayList<>();
                String beanId = element.attributeValue(IocRules.BEAN_RULE.getName());
                String beanClass = element.attributeValue(IocRules.BEAN_RULE.getValue());
                // 保证子元素确实存在
                if (!StringUtils.isEmpty(beanId) && !StringUtils.isEmpty(beanClass)) {
                    // 存放当前bean的className
                    genericBeanDefinition.setClassName(beanClass);
                    // 当前bean的所有子元素
                    List<Element> elements = element.elements();
                    if (!ListUtils.isEmpty(elements)) {
                        for (Element e: elements) {
                            // 如果匹配set注入规则 则注入到容器
                            if (isMatchSetRule(e)) {
                                ChildBeanDefinition childBeanDefinition = ChildBeanDefinition
                                        .builder().childType(SET_INJECT.getType())
                                        .attributeOne(e.attributeValue(SET_INJECT.getName()))
                                        .attributeTwo(e.attributeValue(SET_INJECT.getValue())).build();
                                childBeanDefinitionList.add(childBeanDefinition);
                            } else if (isMatchConstructRule(e)) {
                                ChildBeanDefinition childBeanDefinition = ChildBeanDefinition
                                        .builder().childType(CONS_INJECT.getType())
                                        .attributeOne(e.attributeValue(CONS_INJECT.getName()))
                                        .attributeTwo(e.attributeValue(CONS_INJECT.getValue())).build();
                                childBeanDefinitionList.add(childBeanDefinition);
                            } else {
                                log.error("未按照格式配置xml文件或暂不支持改配置属性");
                            }
                        }
                    } else {
                        log.info("{}下面没有子元素", beanId);
                    }
                    genericBeanDefinition.setChildBeanDefinitionList(childBeanDefinitionList);
                    beanDefinitionXmlMap.put(beanId, genericBeanDefinition);
                }
            }
        }
        return beanDefinitionXmlMap;
    }

    // 是否匹配set注入规则
    private boolean isMatchSetRule(Element e) {
        return e.getName().equals(SET_INJECT.getType())
                && !StringUtils.isEmpty(e.attributeValue(SET_INJECT.getName()))
                && !StringUtils.isEmpty(e.attributeValue(SET_INJECT.getValue()));
    }

    // 是否匹配构造器注入规则
    private boolean isMatchConstructRule(Element e) {
        return e.getName().equals(CONS_INJECT.getType())
                && !StringUtils.isEmpty(e.attributeValue(CONS_INJECT.getName()))
                && !StringUtils.isEmpty(e.attributeValue(CONS_INJECT.getValue()));
    }

    // 是否匹配组件注入规则
    private boolean isMatchScanRule(Element e) {
        return e.getName().equals(SCAN_RULE.getType())
                && !StringUtils.isEmpty(e.attributeValue(SCAN_RULE.getName()));
    }

    /**
     * 根据指定的xml 获得注解扫描的bean容器
     * @param contextConfigLocation
     * @return
     */
    public List<String> getComponentList(String contextConfigLocation) {
        List<String> componentList = new ArrayList<>();
        List<Element> elementList = getElements(contextConfigLocation);
        for (Element e: elementList) {
            if (isMatchScanRule(e)) {
                String packageName = e.attributeValue(SCAN_RULE.getName());
                componentList.addAll(resolveComponentList(packageName));
            }
        }
        return componentList;
    }

    /**
     * 根据要扫描的包名，返回进行相应注解的类
     * @param packageName
     * @return
     */
    public List<String> resolveComponentList(String packageName) {
        if (StringUtils.isEmpty(packageName)) {
            throw new XmlException("请正确设置"+SCAN_RULE.getType()+"的属性");
        }
        List<String> componentList = new ArrayList<>();
        componentList.addAll(ScanUtil.getComponentList(packageName));
        return componentList;
    }

    /**
     * 解析xml 根据路径名获取根元素下面的所有子元素
     * @param contextConfigLocation
     * @return
     */
    private List<Element> getElements(String contextConfigLocation) {
        // 创建saxReader对象
        SAXReader reader = new SAXReader();
        // 通过read方法读取一个文件 转换为Document对象
        Document document = null;
        String pathName = Constants.PATH + contextConfigLocation;
        try {
            document = reader.read(new File(pathName));
        } catch (DocumentException e) {
            e.printStackTrace();
            log.error("文件没有找到,{}", pathName);
        }
        // 获取根结点元素
        Element node = document.getRootElement();
        // 获取所有的bean
        List<Element> elementList = node.elements();
        return elementList;
    }
}
