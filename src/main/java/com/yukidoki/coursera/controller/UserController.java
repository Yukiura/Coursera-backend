package com.yukidoki.coursera.controller;

import com.yukidoki.coursera.Service.LoginService;
import com.yukidoki.coursera.dao.KycMapper;
import com.yukidoki.coursera.dao.UnitMapper;
import com.yukidoki.coursera.dao.UserMapper;
import com.yukidoki.coursera.entity.*;
import com.yukidoki.coursera.utils.IdUtils;
import com.yukidoki.coursera.utils.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserMapper userMapper = SqlSessionUtils.getSqlSession().getMapper(UserMapper.class);
    private final KycMapper kycMapper = SqlSessionUtils.getSqlSession().getMapper(KycMapper.class);
    private final UnitMapper unitMapper = SqlSessionUtils.getSqlSession().getMapper(UnitMapper.class);

    @GetMapping("/id/{id}")
    public User findUserById(@PathVariable("id") int id) {
        return userMapper.findById(id);
    }

    @PostMapping("/register")
    public Integer register(@RequestBody RegisterForm rf) {
        int uid = IdUtils.generate(1);
        int kid = IdUtils.generate(2);
        String avatar = "https://drive.yukidoki.com/apps/files_sharing/publicpreview/tjWdQ2GkpYgT4n5?x=2561&y=749&a=true&file=default.png&scalingup=0";
        User user = new User(uid, rf.getUsername(), passwordEncoder.encode(rf.getPassword()), avatar, "Hello.");
        Kyc kyc = new Kyc(kid, rf.getName(), rf.getCid(), rf.getPhone(), rf.getSid(), unitMapper.getUnitIdByName(rf.getUnit()));
        Integer st1 = userMapper.insertUser(user);
        Integer st2 = kycMapper.insertKyc(kyc);
        Integer st3 = userMapper.insertUserRole(uid, 1);
        int rid = userMapper.getRoleIdByRoleName(rf.getRole());
        Integer st4 = userMapper.insertUserRole(uid, rid);
        Integer st5 = userMapper.insertUserKyc(uid, kid);
        return st1 + st2 + st3 + st4 + st5 == 5 ? 1 : 0;
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

    @GetMapping("/avatar")
    public String getAvatarById() {
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userMapper.getAvatarByUserId(user.getId());
    }

    @GetMapping("/role")
    public List<String> role() {
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userMapper.getRoleListByUserId(user.getId());
    }
}
