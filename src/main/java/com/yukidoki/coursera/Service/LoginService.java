package com.yukidoki.coursera.Service;

import com.yukidoki.coursera.entity.ResponseResult;
import com.yukidoki.coursera.entity.User;

public interface LoginService {
    ResponseResult login(User user);
}
