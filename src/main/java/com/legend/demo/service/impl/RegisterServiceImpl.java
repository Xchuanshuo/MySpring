package com.legend.demo.service.impl;

import com.legend.annotation.Autowired;
import com.legend.annotation.Service;
import com.legend.demo.repository.RegisterDao;
import com.legend.demo.service.RegisterService;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private RegisterDao registerDao;

    @Override
    public void register() {
        registerDao.register();
    }
}
