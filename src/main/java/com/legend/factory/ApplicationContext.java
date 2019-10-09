package com.legend.factory;

import com.legend.exception.XmlException;
import com.legend.utils.FieldUtils;
import com.legend.utils.ReflectionUtil;
import com.legend.xml.FileSystemXmlApplicationContext;
import com.legend.xml.rule.IocRules;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author Legend
 * @data by on 18-10-7.
 * @description 获取上下文
 */
@Slf4j
public class ApplicationContext extends FileSystemXmlApplicationContext implements BeanFactory {

    public Map<String, GenericBeanDefinition> subMap = null;

    public ApplicationContext(String contextConfigLocation) {
        this.subMap = super.getGenericBeanDefinition(contextConfigLocation);
    }

    // 获取实例化后的对象
    @Override
    public Object getBean(String beanId) {
        assert beanId==null : "beanId不存在";
        Object object = null;
        Class<?> clazz = null;
        Set<Map.Entry<String, GenericBeanDefinition>> entries = subMap.entrySet();
        if (!subMap.containsKey(beanId)) {
            throw new XmlException("容器中不存在该bean");
        }
        for (Map.Entry<String, GenericBeanDefinition> entry: entries) {
            if (beanId.equals(entry.getKey())) {
                // 声明一个容器中存在的子对象 用来保存子元素
                GenericBeanDefinition genericBeanDefinition = entry.getValue();
                String beanName = genericBeanDefinition.getClassName();
                // 对象的属性集合
                List<ChildBeanDefinition> childBeanDefinitionList = genericBeanDefinition.getChildBeanDefinitionList();
                try {
                    clazz = Class.forName(beanName);
                } catch (ClassNotFoundException e) {
                    log.error("{}没有找到", beanName);
                    e.printStackTrace();
                }
                object = ReflectionUtil.newInstance(clazz);
                // 遍历属性集合
                for (ChildBeanDefinition childBeanDefinition: childBeanDefinitionList) {
                    // 如果xml中的属性和IocRule中定义的setRule属性一致，则使用set注入
                    if (IocRules.SET_INJECT.getType().equals(childBeanDefinition.getChildType())) {
                        setValue(clazz, childBeanDefinition, object);
                        // 同理 构造方法注入
                    } else if (IocRules.CONS_INJECT.getType().equals(childBeanDefinition.getChildType())) {
                        List<String> paramList = new ArrayList<>();
                        for (ChildBeanDefinition child: childBeanDefinitionList) {
                            if (IocRules.CONS_INJECT.getType().equals(child.getChildType())) {
                                paramList.add(child.getAttributeOne());
                            }
                        }
                        object = consValue(clazz, paramList, object);
                        break;
                    }
                }
            }
        }
        return object;
    }

    /**
     * 构造方法注入
     * @param clazz
     * @param paramList
     * @param object
     * @return
     */
    private Object consValue(Class<?> clazz, List<String> paramList, Object object) {
        Constructor<?> constructor = null;
        Field[] fields = clazz.getDeclaredFields();
        Class<?>[] classArray = new Class[fields.length];
        for (int i=0;i<fields.length;i++) {
            if ("String".equals(fields[i].getType().getSimpleName())) {
                classArray[i] = String.class;
            } else if ("Integer".equals(fields[i].getType().getSimpleName())) {
                classArray[i] = Integer.class;
            } else if ("int".equals(fields[i].getType().getSimpleName())) {
                classArray[i] = int.class;
            }
        }
        try {
            constructor = clazz.getConstructor(classArray);
            object = constructor.newInstance(paramList);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * set注入
     * @param clazz
     * @param childBeanDefinition
     * @param object
     */
    private void setValue(Class<?> clazz, ChildBeanDefinition childBeanDefinition, Object object) {
        Field field = null;
        Method[] methods = null;
        String type = null;
        String propertyName = childBeanDefinition.getAttributeOne();
        String propertyValue = childBeanDefinition.getAttributeTwo();
        // 获取拼接的方法名
        String methodName = FieldUtils.getSetMethodNameByNameField(propertyName);
        // 获取属性的类
        try {
            field = clazz.getDeclaredField(propertyName);
            type = field.getType().getSimpleName();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        methods = clazz.getMethods();
        for (Method method: methods) {
            try {
                if (methodName.equals(method.getName())) {
                    if ("String".equals(type)) {
                        method.invoke(object, propertyValue);
                    } else if ("Integer".equals(type)) {
                        Integer propertyValue2 = Integer.valueOf(propertyValue);
                        method.invoke(object, propertyValue2);
                    } else if ("int".equals(type)) {
                        Integer propertyValue2 = Integer.valueOf(propertyValue);
                        method.invoke(object, propertyValue2);
                    } else {
                        log.error("暂时不支持改属性,{}", type);
                    }
                }
            } catch (IllegalAccessException e) {
                log.error("没有找到该方法名+{}", method.getName());
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
