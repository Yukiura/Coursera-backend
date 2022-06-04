package com.yukidoki.coursera.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Kyc {
    private Integer id;
    private String name;
    private String cid;
    private String phone;
    private String sid;
    private Integer unit;
}