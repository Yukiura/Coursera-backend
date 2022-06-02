package com.yukidoki.coursera.utils;

import com.yukidoki.coursera.entity.Message;
import com.yukidoki.coursera.entity.Student;
import com.yukidoki.coursera.entity.WsMsg;

import java.util.List;
import java.util.Set;

public class WsMsgWrapper {
    public static WsMsg verifySuccessMsg(String receiver) {
        return new WsMsg<>(200, "SYS", "Welcome! " + receiver, "SYS", receiver);
    }

    public static WsMsg<Message> textMsg(Message msg, String sender, String receiver) {
        return new WsMsg<>(200, "TEXT", msg, sender, receiver);
    }

    public static WsMsg<List<Student>> studentListMsg(List<Student> nameList, String receiver) {
        return new WsMsg<>(200, "LIST", nameList, "SYS", receiver);
    }

    public static WsMsg<List<Message>> historyMsg(List<Message> history, String receiver) {
        return new WsMsg<>(200, "HISTORY", history, "SYS", receiver);
    }
}
