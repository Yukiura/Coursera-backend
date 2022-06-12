package com.yukidoki.coursera.entity;

import lombok.Data;

@Data
public class EndpointUser {
    // 用户Id
    private Integer userId;
    // 平台用户名
    private String username;
    // 真实姓名
    private String name;
    // 头像
    private String avatar;
    // 学号/工号
    private String sid;
    // 角色
    private String role;
}
