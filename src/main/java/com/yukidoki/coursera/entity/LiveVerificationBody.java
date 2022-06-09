package com.yukidoki.coursera.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LiveVerificationBody {
    private Integer uid;
    private Integer cid;
}
