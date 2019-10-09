package com.legend.demo.handler;

import com.legend.demo.model.User;
import com.legend.mybatis.IResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Legend
 * @data by on 18-10-14.
 * @description
 */
public class UserListHandler implements IResultHandler<List<User>> {

    @Override
    public List<User> handler(ResultSet resultSet) throws SQLException {
        List<User> list = new ArrayList<>();
        if (resultSet == null) return list;
        while (resultSet.next()) {
            User user = new User();
            user.setUid(resultSet.getInt("uid"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            list.add(user);
        }
        return list;
    }
}
