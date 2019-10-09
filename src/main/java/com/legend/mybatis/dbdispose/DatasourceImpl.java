package com.legend.mybatis.dbdispose;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * @author Legend
 * @data by on 18-10-14.
 * @description 数据库连接池
 */
public class DatasourceImpl implements DataSource {

    private static final Properties properties = new Properties();
    private volatile static Queue<Connection> queue = new ConcurrentLinkedQueue<>();

    static {
        try {
            properties.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("jdbc.properties"));
            Class.forName(properties.getProperty("jdbc.driverClassName"));
            String url = properties.getProperty("jdbc.url");
            String username = properties.getProperty("jdbc.username");
            String password = properties.getProperty("jdbc.password");
            int poolSize = Integer.parseInt(properties.getProperty("jdbc.poolSize"));
            for (int i=0;i<poolSize;i++) {
                Connection connection = DriverManager.getConnection(url, username, password);
                System.out.println("获取到连接: "+connection);
                queue.offer(connection);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private final int poolSize = Integer.parseInt(properties.getProperty("jdbc.poolSize"));

    @Override
    public synchronized Connection getConnection() throws SQLException {
        if (!queue.isEmpty()) {
            Connection connection = queue.poll();
            return (Connection) Proxy.newProxyInstance(DatasourceImpl.class.getClassLoader(),
                    new Class[]{Connection.class}, (proxy, method, args) -> {
                        if ("close".equals(method.getName()) && queue.size()<poolSize) {
                            queue.offer(connection);
                            System.out.println(connection + "被还给Connections数据库连接池了！！");
                            System.out.println("Connections数据库连接池大小为" + queue.size());
                            return null;
                        }
                        if (connection.isClosed() || connection==null) return null;
                        return method.invoke(connection, args);
                    });
        }
        return null;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
