package com.legend.mybatis;

import com.legend.constants.Constants;
import com.legend.demo.repository.UserMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description
 */
@Slf4j
public class XmlBuilderMapper {

    public static final String SELECT = "select";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    public static final String INSERT = "insert";

    public List<MapperInfo> builderMapper(String xmlMapperPath) {
        List<MapperInfo> mapperInfoList = new ArrayList<>();
        // 创建SAXReader对象
        SAXReader reader = new SAXReader();
        Document document = null;
        String pathName = Constants.PATH + xmlMapperPath;
        try {
            document = reader.read(new File(pathName));
        } catch (DocumentException e) {
            log.error("文件没有找到,{}", pathName);
        }
        Element node = document.getRootElement();
        String namespace = node.attributeValue("namespace");
        // 获取所有的bean
        List<Element> elementList = node.elements();
        for (Element element: elementList) {
            MapperInfo mapperInfo = new MapperInfo();
            mapperInfo.setInterfaceName(namespace);
            if (isValid(element.getName())) {
                mapperInfo.setSqlType(element.getName());
                mapperInfo.setMethodName(element.attributeValue("id"));
                String handler = element.attributeValue("resultHandler");
                IResultHandler resultHandler = null;
                try {
                    if (handler != null) {
                        Class<?> clazz = Class.forName(handler);
                        resultHandler = (IResultHandler) clazz.getConstructor().newInstance();
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                mapperInfo.setResultHandler(resultHandler);
                mapperInfo.setSqlContent(element.getText());
                mapperInfoList.add(mapperInfo);
            }
        }
        return mapperInfoList;
    }

    private boolean isValid(String elementName) {
        if (elementName == null) return false;
        if (elementName.equals(SELECT) || elementName.equals(UPDATE)
                || elementName.equals(INSERT) || elementName.equals(DELETE)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        XmlBuilderMapper builderMapper = new XmlBuilderMapper();
        List<MapperInfo> list = builderMapper.builderMapper(Constants.mybatisConfigLocation + "UserMapper.xml");
        for (MapperInfo mapperInfo: list) {
            System.out.println(mapperInfo.getInterfaceName());
            System.out.println(mapperInfo.getMethodName());
            System.out.println(mapperInfo.getSqlContent().trim());
            System.out.println(mapperInfo.getResultHandler());
            System.out.println("------------------");
        }
    }
}
