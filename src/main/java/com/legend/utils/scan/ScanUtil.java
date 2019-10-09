package com.legend.utils.scan;

import com.legend.annotation.Autowired;
import com.legend.annotation.Controller;
import com.legend.annotation.Repository;
import com.legend.annotation.Service;
import com.legend.utils.AnnotationUtils;
import com.legend.utils.ListUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Legend
 * @data by on 18-10-8.
 * @description 注解扫描工具类
 */
@Slf4j
public class ScanUtil {

    private static final String NEED_PROXY = "proxy";
    private static List<String> classNameList = new ArrayList<>();
    private static List<String> componentList = new ArrayList<>();
    private static Map<String, String> interfaceAndImplMap = new ConcurrentHashMap<>();

    /**
     * 获取包下类的全名(包括包名)
     * @param packageName
     * @return
     */
    public static List<String> getClassName(String packageName) {
        Enumeration<URL> urls = null;
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        packageName = packageName.replace(".", "/");
        try {
            urls = contextClassLoader.getResources(packageName);
            while (urls.hasMoreElements()) {
                // 获取class路径
                URL url = urls.nextElement();
                File packageFile = new File(url.getPath());
                log.info("packageFile: {}", packageFile);
                File[] files = packageFile.listFiles();
                if (files == null) break;
                for (File file: files) {
                    // 以class结尾则直接将完整包名.类名添加到列表 否则继续递归查找该路径下的包
                    if (file.getName().endsWith(".class")) {
                        String tmpName = packageName.replace("/", ".")+"."+file.getName();
                        // 获取完整包名+类名 去掉.class
                        String newTmpName = tmpName.substring(0, tmpName.lastIndexOf("."));
                        classNameList.add(newTmpName);
                    } else {
                        String nextPackageName = packageName+ "." + file.getName();
                        getClassName(nextPackageName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classNameList;
    }

    /**
     * 获取需要进行注入的组件
     * @param packageName
     * @return
     */
    public static List<String> getComponentList(String packageName) {
        List<String> classNameList = getClassName(packageName);
        // 组装接口和实现类
        makeInterfaceAndImplMap(classNameList);
        for (String className: classNameList) {
            try {
                resolveComponent(className);
            } catch (ClassNotFoundException e) {
                log.error("扫描注解的时候,{}没有找到", className);
                e.printStackTrace();
            }
        }
        return componentList;
    }

    /**
     * 解析类里面的注解
     * @param className
     * @throws ClassNotFoundException
     */
    public static void resolveComponent(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        // 添加要扫描的注解
        scanAnnotation(Controller.class, clazz);
        scanAnnotation(Service.class, clazz);
        scanAnnotation(Repository.class, clazz);
    }

    /**
     * 扫描注解
     * @param aClass 注解类
     * @param clazz 要扫描的类名
     * @param <A>
     * @throws ClassNotFoundException
     */
    public static <A extends Annotation> void scanAnnotation(Class<A> aClass, Class<?> clazz) throws ClassNotFoundException {
        if (!AnnotationUtils.isEmpty(clazz.getAnnotation(aClass))) {
            Field[] fields = clazz.getDeclaredFields();
            // 如果字段没有Autowired注解 则把类直接放入到列表中
            if (fields==null || fields.length==0 || isEmptyAutowired(fields)) {
                ListUtils.add(componentList, clazz.getName());
            } else {
                for (Field field: fields) {
                    // 如果字段有Autowired注解 继续递归向下进行处理
                    if (field.getAnnotation(Autowired.class) != null) {
                        String fieldName = field.getType().getName();
                        // 如果是接口 则获取它的实现类
                        if (Class.forName(fieldName).isInterface()) {
                            String nextName = convertInterfaceToImpl(fieldName);
                            if (!componentList.contains(nextName)) {
                                resolveComponent(nextName);
                            }
                        } else {
                            resolveComponent(fieldName);
                        }
                    }
                }
                ListUtils.add(componentList, clazz.getName());
            }
            // 如果是需要动态代理注入的接口 也直接加入到列表中
        } else if (clazz.isInterface() && interfaceAndImplMap.get(clazz.getName()).equals(NEED_PROXY)) {
            ListUtils.add(componentList, clazz.getName());
        }
    }
    /**
     * 判断一组属性里面有没有注解
     * @param fields
     * @return
     */
    private static boolean isEmptyAutowired(Field[] fields) {
        for (Field field: fields) {
            if (!AnnotationUtils.isEmpty(field.getAnnotation(Autowired.class))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 组装接口和实现类
     * @param classNameList
     * @return
     */
    private static Map<String, String> makeInterfaceAndImplMap(List<String> classNameList) {
        Class<?> clazz1 =  null;
        // 所有接口名
        List<String> interfaceNameList = new ArrayList<>();
        // 有实现类的接口 没有实现类的需要进行动态注入
        List<String> interfaceExistList = new ArrayList<>();
        for (String name: classNameList) {
            try {
                clazz1 = Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (clazz1.isInterface()) {
                interfaceNameList.add(clazz1.getName());
            }
        }
        for (String name: classNameList) {
            Class<?> clazz2 = null;
            try {
                clazz2 = Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            // 当前类的接口
            Class<?>[] interfaces = clazz2.getInterfaces();
            // 如果是接口的实现类
            if (interfaces!=null && interfaces.length!=0) {
                for (String interfaceName: interfaceNameList) {
                    for (Class<?> interfaceClass: interfaces) {
                        // 如果既有接口 又有实现类 则组装到Map
                        if (interfaceName.equals(interfaceClass.getName())) {
                            interfaceAndImplMap.put(interfaceName, name);
                            interfaceExistList.add(interfaceName);
                        }
                    }
                }
            }
        }
        // 需要动态代理注入的接口 在Map中用value=proxy来识别
        interfaceNameList.removeAll(interfaceExistList);
        if (interfaceNameList!=null && interfaceNameList.size()>0) {
            for (String name: interfaceNameList) {
                interfaceAndImplMap.put(name, NEED_PROXY);
            }
            System.out.println("已经存在的"+interfaceNameList);
        }
        return interfaceAndImplMap;
    }

    /**
     * 根据接口查找实现类
     * @param newFileName
     * @return
     */
    private static String convertInterfaceToImpl(String newFileName) {
        Set<Map.Entry<String, String>> entries = interfaceAndImplMap.entrySet();
        for (Map.Entry<String, String> entry: entries) {
            if (newFileName.equals(entry.getKey()) && !entry.getValue().equals(NEED_PROXY)) {
                return entry.getValue();
            } else if (newFileName.equals(entry.getKey()) && entry.getValue().equals(NEED_PROXY)) {
                return entry.getKey();
            }
        }
        return null;
    }



    public static void main(String[] args) {
        System.out.println(getClassName("com.legend.utils"));
    }
}
