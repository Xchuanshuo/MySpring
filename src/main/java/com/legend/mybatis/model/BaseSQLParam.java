package com.legend.mybatis.model;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-9-21.
 * @description  构造基本的参数类
 */
public class BaseSQLParam {

    // sql语句
    private String sql;
    // 参数列表
    private List<Object> list;

    public BaseSQLParam(String sql) {
        this(sql, null);
    }

    public BaseSQLParam(String sql, List<Object> list) {
        this.sql = sql;
        this.list = list;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }
}
