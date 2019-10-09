package com.legend.mybatis;

import com.legend.mybatis.dbdispose.SqlBaseExecutor;
import com.legend.mybatis.model.BaseSQLParam;
import com.legend.utils.FieldUtils;
import com.legend.utils.TypeUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description 执行器
 */
@Slf4j
public class Executor extends SqlBaseExecutor {

    public Object query(MapperInfo mapperInfo, Object[] parameters) {
        String sql = preDealSql(mapperInfo.getSqlContent());
        IResultHandler resultHandler = mapperInfo.getResultHandler();
        return doQuery(new BaseSQLParam(sql,parameters==null?null:Arrays.asList(parameters))
                , resultHandler);
    }

    public Integer update(MapperInfo mapperInfo, Object[] parameters) {
        String sql = preDealSql(mapperInfo.getSqlContent());
        List<Object> sendParamList = getSendReceiveParamList(mapperInfo.getSqlContent(), parameters);
        // 如果传递的不是基本参数 就利用反射去构造需要传递的参数列表
        if (sendParamList.size() > 0) {
            return doUpdate(new BaseSQLParam(sql, sendParamList));
        }
        return doUpdate(new BaseSQLParam(sql, Arrays.asList(parameters)));
    }

    private String preDealSql(String preSql) {
        String regex = "#\\{.*?}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(preSql);
        System.out.println(matcher.results());
        String sql = matcher.replaceAll("?").trim();
        return sql;
    }

    /**
     * 得到SQL语句需要接收的参数列表
     * @param preSql
     * @return
     */
    private List<String> getReceiveParamList(String preSql) {
        String regex = "\\{(.*?)}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(preSql);
        List<String> paramsList = new ArrayList<>();
        while (matcher.find()) {
            String str = matcher.group().replace("{","")
                    .replace("}","");
            paramsList.add(str);
        }
        return paramsList;
    }

    /**
     * 处理需要传送的参数列表
     * @param preSql
     * @param parameters
     * @return
     */
    private List<Object> getSendReceiveParamList(String preSql, Object[] parameters) {
        List<Object> sendParamList = new ArrayList<>();
        if (parameters != null && parameters.length!=0) {
            Object object = parameters[0];
            Class<?> clazz = object.getClass();
            if (!TypeUtils.isBasicType(clazz.getSimpleName())) {
                // SQL语句接收的参数列表
                List<String> receiveParamList = getReceiveParamList(preSql);
                for (String param: receiveParamList) {
                    try {
                        String methodName = FieldUtils.getGetMethodNameByNameField(param);
                        Method method = object.getClass().getMethod(methodName);
                        sendParamList.add(method.invoke(object));
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sendParamList;
    }
}
