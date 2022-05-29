package com.yukidoki.coursera.dao;

import com.yukidoki.coursera.entity.User;

public interface UserMapper {
    User findById(int id);

    User findByUsername(String username);

    Integer insertUser(User user);
}
