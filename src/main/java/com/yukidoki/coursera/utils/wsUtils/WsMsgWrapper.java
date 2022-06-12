package com.yukidoki.coursera.utils.wsUtils;

import com.yukidoki.coursera.entity.Message;
import com.yukidoki.coursera.entity.EndpointUser;
import com.yukidoki.coursera.utils.wsUtils.WsMsg;

import java.util.List;

public class WsMsgWrapper {
    public static WsMsg verifySuccessMsg() {
        return new WsMsg<>(200, "SYS", "Welcome! ");
    }

    public static WsMsg<Message> textMsg(Message msg) {
        return new WsMsg<>(200, "TEXT", msg);
    }

    public static WsMsg<List<EndpointUser>> studentListMsg(List<EndpointUser> nameList) {
        return new WsMsg<>(200, "LIST", nameList);
    }

    public static WsMsg<List<EndpointUser>> signInNameListMsg(List<EndpointUser> nameList) {
        return new WsMsg<>(200, "SIGN_IN_LIST", nameList);
    }

    public static WsMsg<List<Message>> historyMsg(List<Message> history) {
        return new WsMsg<>(200, "HISTORY", history);
    }

    public static WsMsg<String> countDownMessage(String timeLeft) {
        return new WsMsg<>(200, "COUNTDOWN", timeLeft);
    }

    public static WsMsg<Integer> signInStatus(Integer status) {
        return new WsMsg<>(200, "SIGN_IN_STATUS", status);
    }
}
