package com.legend.demo.repository;

import com.legend.demo.model.User;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description
 */
public interface UserMapper {

    List<User> queryAllUser();

    Integer createUser(User user);

    User queryUser(String username, String password);

    Integer updateUserById(User user);

    Integer deleteUserById(int id);
}
