package com.yukidoki.coursera.entity;

import lombok.Data;

@Data
public class RegisterForm {
    private String username;
    private String password;
    private String checkPass;
    private String name;
    private String cid;
    private String sid;
    private String role;
    private String phone;
    private String unit;
}
