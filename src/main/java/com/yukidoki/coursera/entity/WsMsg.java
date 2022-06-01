package com.yukidoki.coursera.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WsMsg<T> {
    // 状态码
    private Integer code;
    // 消息类型
    String type;
    // 消息数据
    private T data;
    // 消息发送者
    private String sender;
    // 消息接收者
    private String receiver;
}
