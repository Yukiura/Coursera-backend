package com.yukidoki.coursera.ws;

import com.google.gson.Gson;
import com.yukidoki.coursera.dao.ClassroomMapper;
import com.yukidoki.coursera.dao.MessageMapper;
import com.yukidoki.coursera.dao.UserMapper;
import com.yukidoki.coursera.entity.Message;
import com.yukidoki.coursera.entity.Student;
import com.yukidoki.coursera.entity.User;
import com.yukidoki.coursera.entity.WsMsg;
import com.yukidoki.coursera.utils.JwtUtils;
import com.yukidoki.coursera.utils.SqlSessionUtils;
import com.yukidoki.coursera.utils.WsMsgWrapper;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@ServerEndpoint(value = "/chat")
@Component
public class ChatEndpoint {
    private static final Map<String, ChatEndpoint> endpointMap = new HashMap<>();
    private static final UserMapper userMapper = SqlSessionUtils.getSqlSession().getMapper(UserMapper.class);
    private static final MessageMapper messageMapper = SqlSessionUtils.getSqlSession().getMapper(MessageMapper.class);
    private static final ClassroomMapper classroomMapper = SqlSessionUtils.getSqlSession().getMapper(ClassroomMapper.class);
    private static final Gson gson = new Gson();
    private Session session;
    private String username;
    private Integer classId;
    private Student student;
    private List<Student> classMates;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String msg, Session session) throws IOException {
        WsMsg wsMsg = gson.fromJson(msg, WsMsg.class);
        String msgType = wsMsg.getType();
        switch (msgType) {
            case "VERIFY":  // 学生进入直播教室需要先验证身份
                String token = (String) wsMsg.getData();
                // 解析token
                int userId = Integer.parseInt(JwtUtils.parseJwt(token));
                User user = userMapper.findById(userId);
                this.student = userMapper.getStudentByUserId(userId);
                if (user == null) {
                    throw new RuntimeException();
                }
                this.username = user.getUsername();
                this.session.getBasicRemote().sendText(gson.toJson(WsMsgWrapper.verifySuccessMsg(username)));
                endpointMap.put(this.username, this);
                break;
            case "CLASS":   // 学生刚进入直播教室时前端需传递班级号
                System.out.println("班级ID: " + wsMsg.getData());
                this.classId = Integer.parseInt((String) wsMsg.getData());
                // 向新加入直播教室的同学发送聊天历史信息
                List<Message> history = messageMapper.getMessageListByClassId(this.classId);
                this.session.getBasicRemote().sendText(gson.toJson(WsMsgWrapper.historyMsg(history, this.username)));
                // 获取在线学生名单
                this.classMates = classroomMapper.getStudentListByClassId(this.classId);
                for (Student stu : classMates) {
                    stu.setOnline(endpointMap.get(stu.getUsername()) != null);
                }
                // 向同班同学广播在线学生名单
                for (ChatEndpoint endpoint : endpointMap.values()) {
                    if (Objects.equals(endpoint.classId, this.classId)) {
                        endpoint.session.getBasicRemote().sendText(gson.toJson(WsMsgWrapper.studentListMsg(this.classMates, endpoint.username)));
                    }
                }
                break;
            case "TEXT":    // 学生在聊天板块中发布消息
                // 将消息封装为Message对象
                Message msgObject = new Message();
                String text = (String) wsMsg.getData();
                msgObject.setSender(this.student.getName());
                msgObject.setText(text);
                msgObject.setToclass(this.classId);
                msgObject.setTime(new Date());
                // 将消息放入数据库
                messageMapper.insertMessage(msgObject);
                // 向同班同学广播消息
                for (ChatEndpoint endpoint : endpointMap.values()) {
                    if (Objects.equals(endpoint.classId, this.classId)) {
                        endpoint.session.getBasicRemote().sendText(gson.toJson(WsMsgWrapper.textMsg(msgObject, this.username, endpoint.username)));
                    }
                }
                break;
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        endpointMap.remove(this.username);
        // 向同班同学广播在线学生名单
        for (Student stu : classMates) {
            stu.setOnline(endpointMap.get(stu.getUsername()) != null);
        }
        for (ChatEndpoint endpoint : endpointMap.values()) {
            if (Objects.equals(endpoint.classId, this.classId)) {
                endpoint.session.getBasicRemote().sendText(gson.toJson(WsMsgWrapper.studentListMsg(this.classMates, endpoint.username)));
            }
        }
    }
}
