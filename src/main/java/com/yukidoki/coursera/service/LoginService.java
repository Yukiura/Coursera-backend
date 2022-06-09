package com.yukidoki.coursera.service;

import com.yukidoki.coursera.entity.ResponseResult;
import com.yukidoki.coursera.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
