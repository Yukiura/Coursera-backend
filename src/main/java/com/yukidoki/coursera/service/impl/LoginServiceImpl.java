package com.yukidoki.coursera.service.impl;

import com.yukidoki.coursera.service.LoginService;
import com.yukidoki.coursera.entity.LoginUser;
import com.yukidoki.coursera.entity.ResponseResult;
import com.yukidoki.coursera.entity.User;
import com.yukidoki.coursera.utils.JwtUtils;
import com.yukidoki.coursera.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        var authentication = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authentication)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getId().toString();
        String jwt = JwtUtils.getJwt(userId);
        //authenticate存入redis
        redisCache.setCacheObject("login:" + userId, loginUser);
        //把token响应给前端
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return new ResponseResult(200, "登录成功", map);
    }

    @Override
    public ResponseResult logout() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        var userId = loginUser.getId().toString();
        redisCache.deleteObject("login:" + userId);
        return new ResponseResult(200, "登出成功", null);
    }
}
