package com.legend.factory;

import com.legend.annotation.Aspect;
import com.legend.annotation.Autowired;
import com.legend.annotation.PointCut;
import com.legend.constants.Constants;
import com.legend.mybatis.MySqlSession;
import com.legend.utils.AnnotationUtils;
import com.legend.utils.ClassUtils;
import com.legend.utils.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Legend
 * @data by on 18-10-8.
 * @description
 */
@Slf4j
public class InitBean extends BeanDefinition {

    // 初始化后的bean容器 key为class名 value为实例对象
    public static Map<String, Object> beanContainerMap = new ConcurrentHashMap<>();

    /**
     *
     */
    public void initBeans() {
        // 初始化xml配置
        initXmlBeans(Constants.contextConfigLocation);
        initXmlBeans(Constants.springmvcConfigLocation);
        // 初始化扫描注解的配置
        initAutowiredBeans(Constants.contextConfigLocation);
        // 扫描aop容器
        initAopBean(Constants.aopConfig);
    }

    /**
     * 初始化xml中的bean
     * @param contextConfigLocation
     */
    public void initXmlBeans(String contextConfigLocation) {
        ApplicationContext applicationContext = new ApplicationContext(contextConfigLocation);
        Class<?> clazz= null;
        // 从容器中取出bean 用application的getBean方法依次加载bean
        Map<String, GenericBeanDefinition> beanDefinitionMap = super.getBeanDefinitionXmlMap(contextConfigLocation);
        Set<Map.Entry<String, GenericBeanDefinition>> entries = beanDefinitionMap.entrySet();
        for (Map.Entry<String, GenericBeanDefinition> entry: entries) {
            String beanId = entry.getKey();
            String className = entry.getValue().getClassName();
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                log.error("xml中{}无法实例化", className);
                e.printStackTrace();
            }
            beanContainerMap.put(className, clazz.cast(applicationContext.getBean(beanId)));
        }
    }

    /**
     * 将componentList(加注解的类)所有的bean实例化
     * @param contextConfigLocation
     */
    public void initAutowiredBeans(String contextConfigLocation) {
        List<String> componentList = super.getComponentList(contextConfigLocation);
        System.out.println("实例化的顺序"+componentList);
        // 扫描所有有注解的类 并进行初始化
        for (String className: componentList) {
            try {
                initClass(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化每一个类的方法
     * @param className
     */
    public void initClass(String className) throws ClassNotFoundException, IllegalAccessException {
        Class<?> clazz = Class.forName(className);
        // 类如果有接口 则将接口装配
        Class<?>[] interfaces = clazz.getInterfaces();
        if (clazz.isInterface()) {
            // 如果是接口 则注入的对象是动态代理的对象
            String xmlName = Constants.mybatisConfigLocation+clazz.getSimpleName()+".xml";
            File file = new File(Constants.PATH+xmlName);
            if (file.exists()) {
                MySqlSession mySqlSession = new MySqlSession();
                beanContainerMap.put(clazz.getName(), mySqlSession.getMapper(clazz, xmlName));
            }
        } else if (interfaces==null || interfaces.length==0) {
            noInterfaceInit(className, className);
        } else {
            for (Class<?> interfaceClass: interfaces)
                // 容器中有该类 直接使用这个对象进行装配
                if (isExistInContainer(className)) {
                    beanContainerMap.put(interfaceClass.getName(), beanContainerMap.get(className));
                } else {
                    // 容器不存在该类时 先实例化实现类 然后进行装配
                    noInterfaceInit(className, interfaceClass.getName());
                }
        }
    }

    /**
     * 无接口的情况下进行类初始化
     * @param className
     * @param interfaceName
     */
    public void noInterfaceInit(String className, String interfaceName) throws ClassNotFoundException, IllegalAccessException {
        Class<?> clazz = Class.forName(className);
        if (beanContainerMap.containsKey(interfaceName)) {
            return;
        }
        // bean实例化
        System.out.println("实例化的名字"+clazz.getName());
        Object object = ReflectionUtil.newInstance(clazz);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            // 如果字段上有Autowired注解 则先将属性注入进去
            if (!AnnotationUtils.isEmpty(field.getAnnotation(Autowired.class))) {
                field.setAccessible(true);
                Set<Map.Entry<String, Object>> entries = beanContainerMap.entrySet();
                for (Map.Entry<String, Object> entry: entries) {
                    String type = field.getType().getName();
                    if (entry.getKey().equals(type)) {
                        field.set(object, entry.getValue());
                    }
                }
            }
        }
        beanContainerMap.put(interfaceName, object);
    }

    /**
     * 实例化类之前判断类在容器中是否存在
     * @param className
     * @return
     */
    public boolean isExistInContainer(String className) {
        if (beanContainerMap.containsKey(className)) {
            return true;
        }
        return false;
    }

    /**
     * Aop扫描
     * @param basePath
     */
    private void initAopBean(String basePath) {
        Set<Class<?>> classSet = ClassUtils.getClassSet(basePath);
        for (Class clazz: classSet) {
            if (clazz.isAnnotationPresent(Aspect.class)) {
                // 找到切面
                Method[] methods = clazz.getMethods();
                for (Method method: methods) {
                    if (method.isAnnotationPresent(PointCut.class)) {
                        // 找到切点
                        PointCut pointCut = (PointCut) method.getAnnotations()[0];
                        String pointCutStr = pointCut.value();
                        String[] pointCutArr = pointCutStr.split("_");

                        // 被代理的类名
                        String className = pointCutArr[0];
                        // 被代理的方法名
                        String methodName = pointCutArr[1];
                        // 根据切点 创建被代理对象
                        Object targetObj = ReflectionUtil.newInstance(className);
                        // 根据切面创建代理者
                        AbsMethodAdvance proxyer = (AbsMethodAdvance) ReflectionUtil.newInstance(clazz);
                        proxyer.setProxyMethodName(methodName);
                        // 设置代理的方法
                        Object object = proxyer.createProxyObject(targetObj);
                        if (object != null) {
                            beanContainerMap.put(targetObj.getClass().getSimpleName(), object);
                        }
                    }
                }
            }
        }
    }
}
