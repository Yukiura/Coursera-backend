package com.yukidoki.coursera.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Classroom {
    private Integer id;
    private String name;
    private Date begin;
    private Date end;
    private String code;
}
