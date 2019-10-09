package com.legend.demo.service.impl;

import com.legend.annotation.Autowired;
import com.legend.annotation.Service;
import com.legend.demo.repository.RegisterDao;
import com.legend.demo.repository.UserDao;
import com.legend.demo.repository.UserMapper;
import com.legend.demo.service.UserService;
import com.legend.demo.model.User;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RegisterDao registerDao;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> queryAllUser() {
        return userMapper.queryAllUser();
    }

    @Override
    public User queryUser(String username, String password) {
        return userMapper.queryUser(username, password);
    }

    @Override
    public Integer createUser(User user) {
        return userMapper.createUser(user);
    }

    @Override
    public Integer updateUserById(User user) {
        return userMapper.updateUserById(user);
    }

    @Override
    public Integer deleteUserById(int uid) {
        return userMapper.deleteUserById(uid);
    }
}
