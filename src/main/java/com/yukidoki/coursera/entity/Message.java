package com.yukidoki.coursera.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private Integer id;
    private String text;
    private Date time;
    private Integer sender;
    private Integer toclass;
}
