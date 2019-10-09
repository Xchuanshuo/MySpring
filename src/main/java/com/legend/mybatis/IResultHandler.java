package com.legend.mybatis;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Legend
 * @data by on 18-10-14.
 * @description
 */
public interface IResultHandler<T> {

    T handler(ResultSet resultSet) throws SQLException;
}
