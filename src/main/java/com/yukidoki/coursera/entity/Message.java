package com.yukidoki.coursera.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private String text;
    private Date time;
    private String sender;
    private Integer toclass;
}
