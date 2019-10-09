package com.legend.demo.handler;

import com.legend.demo.model.Boy;
import com.legend.mybatis.IResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Legend
 * @data by on 18-10-15.
 * @description
 */
public class BoyListHandler implements IResultHandler<List<Boy>> {
    @Override
    public List<Boy> handler(ResultSet resultSet) throws SQLException {
        List<Boy> list = new ArrayList<>();
        if (resultSet == null) return list;
        while (resultSet.next()) {
            Boy boy = new Boy();
            boy.setId(resultSet.getInt("id"));
            boy.setAge(resultSet.getInt("age"));
            boy.setName(resultSet.getString("name"));
            list.add(boy);
        }
        return list;
    }
}
