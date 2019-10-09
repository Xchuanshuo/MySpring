package com.legend.springmvc;

import com.legend.annotation.Controller;
import com.legend.annotation.RequestMapping;
import com.legend.exception.SpringMVCException;
import com.legend.utils.AnnotationUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description 遍历bean容器 在有controller注解的类中有RequestMapping扫描的方法 则
 *              将方法和URL绑定
 */
@Slf4j
public class Handler {

    public static Map<String, Method> bindingRequestMapping(Map<String, Object> beanContainerMap) {
        if (beanContainerMap == null) {
            throw new SpringMVCException("实例化bean异常, 没有找容器");
        }
        Map<String, Method> handlerMapping = new ConcurrentHashMap<>();
        Set<Map.Entry<String, Object>> entries = beanContainerMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Class clazz = entry.getValue().getClass();
            Annotation annotation = clazz.getAnnotation(Controller.class);
            Method[] methods = clazz.getMethods();
            if (!AnnotationUtils.isEmpty(annotation) && methods != null && methods.length!=0) {
                for (Method method: methods) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    if (!AnnotationUtils.isEmpty(requestMapping)) {
                        String key = requestMapping.value();
                        handlerMapping.put(key, method);
                    }
                }
            }
        }
        return handlerMapping;
    }
}