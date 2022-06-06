package com.yukidoki.coursera.dao;

import com.yukidoki.coursera.entity.Student;
import com.yukidoki.coursera.entity.User;

import java.util.List;

public interface UserMapper {
    User findById(int id);

    User findByUsername(String username);

    Integer insertUser(User user);

    Student getStudentByUserId(Integer id);

    String getAvatarByUserId(Integer id);

    Integer insertUserRole(Integer uid, Integer rid);

    Integer insertUserKyc(Integer uid, Integer kid);

    Integer getRoleIdByRoleName(String name);

    List<String> getRoleListByUserId(Integer id);
}
