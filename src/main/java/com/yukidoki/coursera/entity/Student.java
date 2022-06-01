package com.yukidoki.coursera.entity;

import lombok.Data;

@Data
public class Student {
    // 学生的平台用户名
    private String username;
    // 学生的真实姓名
    private String name;
    // 学生用户的头像
    private String avatar;
    // 学生的学号
    private String sid;
    // 是否在线, 默认离线
    private boolean online = false;
}
