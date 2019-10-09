package com.legend.demo.repository.impl;

import com.legend.annotation.Repository;
import com.legend.demo.repository.UserDao;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Override
    public void test() {
        System.out.println("我是UserDao");
    }
}
