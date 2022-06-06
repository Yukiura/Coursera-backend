package com.yukidoki.coursera.entity;

import lombok.Data;

@Data
public class SearchResult {
    // 课程id
    private Integer cid;
    // 课程名称
    private String name;
    // 课程封面
    private String cover;
    // 课程描述
    private String description;
    // 学校id
    private Integer sid;
    // 学校名称
    private String school;
    // 学校logo
    private String logo;
}
