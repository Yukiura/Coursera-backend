package com.yukidoki.coursera.dao;

import com.yukidoki.coursera.entity.Message;

import java.util.List;

public interface MessageMapper {
    Integer insertMessage(Message message);

    List<Message> getMessageListByClassId(Integer id);
}
