package com.legend.mybatis;

import com.legend.utils.ListUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import static com.legend.mybatis.XmlBuilderMapper.SELECT;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description 使用动态代理将Mapper接口和xml关联
 */
public class MapperProxy implements InvocationHandler {

    private MySqlSession sqlSession;
    private String xmlName;

    public MapperProxy(MySqlSession sqlSession, String xmlName) {
        this.sqlSession = sqlSession;
        this.xmlName = xmlName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        XmlBuilderMapper xmlBuilderMapper = new XmlBuilderMapper();
        List<MapperInfo> mapperInfoList = xmlBuilderMapper.builderMapper(xmlName);
        if (!ListUtils.isEmpty(mapperInfoList)) {
            for (MapperInfo mapperInfo: mapperInfoList) {
                if (!method.getDeclaringClass().getName().equals(mapperInfo.getInterfaceName())) {
                    return null;
                }
                if (method.getName().equals(mapperInfo.getMethodName())) {
                    // 只有select方法需要返回结果集 其它只需要执行状态即可
                    if (!mapperInfo.getSqlType().equals(SELECT)) {
                        return sqlSession.update(mapperInfo, args);
                    }
                    return sqlSession.select(mapperInfo, args);
                }
            }
        }
        return null;
    }
}
