package com.yukidoki.coursera.controller;

import com.yukidoki.coursera.Service.LoginService;
import com.yukidoki.coursera.dao.UserMapper;
import com.yukidoki.coursera.entity.ResponseResult;
import com.yukidoki.coursera.entity.User;
import com.yukidoki.coursera.utils.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private LoginService loginService;

    private final UserMapper userMapper = SqlSessionUtils.getSqlSession().getMapper(UserMapper.class);

    @GetMapping("/id/{id}")
    public User findUserById(@PathVariable("id") int id) {
        return userMapper.findById(id);
    }

    @PostMapping("/register")
    public Integer register(User user) {
        return userMapper.insertUser(user);
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {
        return loginService.login(user);
    }

    @RequestMapping("/logout")
    public ResponseResult logout() {
        return loginService.logout();
    }

    @GetMapping("/hello")
    public String hello() {
        return "gm";
    }
}
