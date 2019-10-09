package com.legend.mybatis.dbdispose;

import com.legend.mybatis.IResultHandler;
import com.legend.mybatis.model.BaseSQLParam;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description
 */
public class SqlBaseExecutor {

    // 执行成功
    public static final int EXECUTE_SUCCESS = 1;
    // 执行失败
    public static final int EXECUTE_FAILURE = 0;
    // 执行不合法
    public static final int EXECUTE_ILLEGAL = -1;

    // 重载一个带connection的方法 方便进行批量操作时代码的复用
    private Integer doUpdate(Connection connection, BaseSQLParam param) {
        boolean isPatchUpdate = false;
        PreparedStatement ps = null;
        try {
            if (connection == null) {
                connection = DBManager.getConnection();
            } else {
                isPatchUpdate = true;
            }
            List<Object> params = param.getList();
            ps = connection.prepareStatement(param.getSql());
            if (params != null && params.size()>0) {
                for (int i=0;i<params.size();i++) {
                    ps.setObject(i+1, params.get(i));
                }
            }
            ps.execute();
            return EXECUTE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return EXECUTE_FAILURE;
        } finally {
            // 不是批量操作时才关闭连接
            if (!isPatchUpdate) {
                closeConnection(connection);
                closeStatement(ps);
            }
        }
    }

    // 创建 删除 or 更新
    protected Integer doUpdate(BaseSQLParam param) {
        return doUpdate(null, param);
    }

    // 查询操作
    protected <T> T doQuery(BaseSQLParam param, IResultHandler<T> resultHandler) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = DBManager.getConnection();
            if (connection != null) {
                List<Object> params = param.getList();
                ps = connection.prepareStatement(param.getSql());
                if (params != null && params.size()>0) {
                    for (int i=0;i<params.size();i++) {
                        ps.setObject(i+1, params.get(i));
                    }
                }
                if (ps != null) {
                    resultSet = ps.executeQuery();
                }
            }
            return resultHandler.handler(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeResultSet(resultSet);
            closeStatement(ps);
            DBManager.closeConnection();
        }
    }

    // 批量操作 事务
    protected Integer doPatchUpdate(List<BaseSQLParam> params) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DBManager.getConnection();
            DBManager.startTransaction();
            for (BaseSQLParam param: params) {
                int result = doUpdate(connection, param);
                if (result != EXECUTE_SUCCESS) {
                    throw new Exception("执行出错!");
                }
            }
            // 全部执行成功后提交
            DBManager.commitTransaction();
            return EXECUTE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollback();
            return EXECUTE_FAILURE;
        } finally {
            DBManager.closeConnection();
            closeStatement(ps);
        }
    }

    private void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeStatement(PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
