package com.yukidoki.coursera.Service.impl;

import com.yukidoki.coursera.dao.UserMapper;
import com.yukidoki.coursera.entity.LoginUser;
import com.yukidoki.coursera.entity.User;
import com.yukidoki.coursera.utils.SqlSessionUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper = SqlSessionUtils.getSqlSession().getMapper(UserMapper.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        User user = userMapper.findByUsername(username);

        //如果查询不到数据就通过抛出异常来给出提示
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户名或密码错误");
        }

        //TODO 根据用户查询权限信息 添加到LoginUser中

        //封装成UserDetails对象返回
        return new LoginUser(user);
    }
}
