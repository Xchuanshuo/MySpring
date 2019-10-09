package com.legend.demo.handler;

import com.legend.demo.model.User;
import com.legend.mybatis.IResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Legend
 * @data by on 18-10-14.
 * @description
 */
public class UserHandler implements IResultHandler<User> {
    @Override
    public User handler(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            User user = new User();
            user.setUid(resultSet.getInt("uid"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            return user;
        }
        return null;
    }
}
