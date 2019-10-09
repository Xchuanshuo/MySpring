package com.legend.constants;

import com.legend.xml.FileSystemXmlApplicationContext;

/**
 * @author Legend
 * @data by on 18-10-7.
 * @description 保存各个配置文件的路径
 */
public interface Constants {
    String PATH = FileSystemXmlApplicationContext.class.getResource("/").getPath();
    String contextConfigLocation = "application.xml";
    String springmvcConfigLocation = "springmvc.xml";
    String mybatisConfigLocation = "mapper/";
    String aopConfig = "com.legend.demo.aspect";
}
