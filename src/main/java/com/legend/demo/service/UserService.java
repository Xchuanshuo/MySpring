package com.legend.demo.service;

import com.legend.demo.model.User;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description
 */
public interface UserService {

    List<User> queryAllUser();

    User queryUser(String username, String password);

    Integer createUser(User user);

    Integer updateUserById(User user);

    Integer deleteUserById(int uid);
}
