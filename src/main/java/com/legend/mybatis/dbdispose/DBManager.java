package com.legend.mybatis.dbdispose;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Legend
 * @data by on 18-10-15.
 * @description
 */
public class DBManager {

    private static DatasourceFactory factory = DatasourceFactory.getInstance();
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    public static Connection getConnection() {
        Connection connection = threadLocal.get();
        if (connection == null) {
            connection = factory.getSession();
            threadLocal.set(connection);
        }
        return connection;
    }

    public static void startTransaction() {
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void commitTransaction() {
        try {
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollback() {
        try {
            getConnection().rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        Connection connection = threadLocal.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                threadLocal.remove();
            }
        }
    }
}
