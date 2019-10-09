package com.legend.demo.repository.impl;

import com.legend.annotation.Repository;
import com.legend.demo.model.User;
import com.legend.demo.repository.RegisterDao;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description
 */
@Repository
public class RegisterDaoImpl implements RegisterDao {

    @Override
    public void register() {
        System.out.println("我是RegisterDao");
    }
}
