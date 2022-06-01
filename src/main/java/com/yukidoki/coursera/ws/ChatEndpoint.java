package com.yukidoki.coursera.ws;

import com.google.gson.Gson;
import com.yukidoki.coursera.dao.MessageMapper;
import com.yukidoki.coursera.dao.UserMapper;
import com.yukidoki.coursera.entity.Message;
import com.yukidoki.coursera.entity.User;
import com.yukidoki.coursera.entity.WsMsg;
import com.yukidoki.coursera.utils.JwtUtils;
import com.yukidoki.coursera.utils.SqlSessionUtils;
import com.yukidoki.coursera.utils.WsMsgWrapper;
import org.springframework.stereotype.Component;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@ServerEndpoint(value = "/chat")
@Component
public class ChatEndpoint {

    private static final Set<ChatEndpoint> endpointSet = new HashSet<>();
    private static final UserMapper userMapper = SqlSessionUtils.getSqlSession().getMapper(UserMapper.class);
    private static final MessageMapper messageMapper = SqlSessionUtils.getSqlSession().getMapper(MessageMapper.class);
    private Session session;
    private Integer userId;
    private String username;
    private Integer classId;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String msg, Session session) throws IOException {
        Gson gson = new Gson();
        WsMsg wsMsg = gson.fromJson(msg, WsMsg.class);
        String msgType = wsMsg.getType();
        switch (msgType) {
            case "VERIFY":  // 学生进入直播教室需要先验证身份
                String token = (String) wsMsg.getData();
                // 解析token
                this.userId = Integer.parseInt(JwtUtils.parseJwt(token));
                User user = userMapper.findById(this.userId);
                if (user == null) {
                    throw new RuntimeException();
                }
                this.userId = user.getId();
                this.username = user.getUsername();
                this.session.getBasicRemote().sendText(gson.toJson(WsMsgWrapper.verifySuccessMsg(username)));
                endpointSet.add(this);
                break;
            case "CLASS":   // 学生刚进入直播教室时前端需传递班级号
                this.classId = Integer.parseInt((String) wsMsg.getData());
                // 向新加入直播教室的同学发送聊天历史信息
                List<Message> history = messageMapper.getMessageListByClassId(this.classId);
                this.session.getBasicRemote().sendText(gson.toJson(WsMsgWrapper.historyMsg(history, this.username)));
                // 获取在线学生名单
                List<String> studentList = new ArrayList<>();
                for (ChatEndpoint endpoint : endpointSet) {
                    if (Objects.equals(endpoint.classId, this.classId)) {
                        studentList.add(endpoint.username);
                    }
                }
                // 向同班同学广播在线学生名单
                for (ChatEndpoint endpoint : endpointSet) {
                    if (Objects.equals(endpoint.classId, this.classId)) {
                        endpoint.session.getBasicRemote().sendText(gson.toJson(WsMsgWrapper.studentListMsg(studentList, endpoint.username)));
                    }
                }
                break;
            case "TEXT":    // 学生在聊天板块中发布消息
                // 将消息封装为Message对象
                Message msgToInsert = new Message();
                String text = (String) wsMsg.getData();
                msgToInsert.setSender(this.userId);
                msgToInsert.setText(text);
                msgToInsert.setToclass(this.classId);
                msgToInsert.setTime(new Date());
                // 将消息放入数据库
                messageMapper.insertMessage(msgToInsert);
                // 向同班同学广播消息
                for (ChatEndpoint endpoint : endpointSet) {
                    if (Objects.equals(endpoint.classId, this.classId)) {
                        endpoint.session.getBasicRemote().sendText(gson.toJson(WsMsgWrapper.textMsg(text, this.username, endpoint.username)));
                    }
                }
                break;
        }
    }
}
