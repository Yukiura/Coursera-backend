package com.yukidoki.coursera.entity;

import lombok.*;

@Data
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
    private String avatar;
    private String bio;
}
