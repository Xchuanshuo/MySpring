package com.legend.mybatis.dbdispose;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Legend
 * @data by on 18-10-15.
 * @description
 */
public class DatasourceFactory {

    private volatile static DatasourceFactory factory;
    private DataSource dataSource;

    private DatasourceFactory() {
        dataSource = new DatasourceImpl();
    }

    public static DatasourceFactory getInstance() {
        if (factory == null) {
            synchronized (DatasourceFactory.class) {
                if (factory == null) {
                    factory = new DatasourceFactory();
                }
            }
        }
        return factory;
    }

    public DataSource getDatasource() {
        return dataSource;
    }

    public Connection getSession() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws SQLException {
        for (int i=0;i<10;i++) {
            new Thread(new Task(i)).start();
        }
    }

    static class Task implements Runnable {

        private int count;
        private DatasourceFactory factory = DatasourceFactory.getInstance();

        Task(int count) {
            this.count = count;
        }

        @Override
        public void run() {
            for (int i=0;i<10000;i++) {
                Connection connection = factory.getSession();
                System.out.println("Thread-" + count + " get session "+connection);
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
