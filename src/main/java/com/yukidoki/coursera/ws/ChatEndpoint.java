package com.yukidoki.coursera.ws;

import com.google.gson.Gson;
import com.yukidoki.coursera.dao.ClassroomMapper;
import com.yukidoki.coursera.dao.MessageMapper;
import com.yukidoki.coursera.dao.UserMapper;
import com.yukidoki.coursera.entity.*;
import com.yukidoki.coursera.utils.JwtUtils;
import com.yukidoki.coursera.utils.SqlSessionUtils;
import com.yukidoki.coursera.utils.wsUtils.WsMsg;
import com.yukidoki.coursera.utils.wsUtils.WsMsgWrapper;
import com.yukidoki.coursera.utils.wsUtils.signInUtils.SignInCounter;
import org.quartz.SchedulerException;
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
    // 每个班级的签到计时器
    private static final Map<Integer, Integer> signInCounterMap = new HashMap<>();
    // 每个班级的签到名单
    private static final Map<Integer, Map<Integer, EndpointUser>> signInMap = new HashMap<>();
    // 每个班级对应的老师终端列表
    private static final Map<Integer, Map<Integer, ChatEndpoint>> classroomTeachersMap = new HashMap<>();
    // 每个班级对应的学生终端列表
    private static final Map<Integer, Map<Integer, ChatEndpoint>> classroomStudentsMap = new HashMap<>();
    // 每个班级的在线成员名单
    private static final Map<Integer, Map<Integer, EndpointUser>> classroomNameListMap = new HashMap<>();
    private static final UserMapper userMapper = SqlSessionUtils.getSqlSession().getMapper(UserMapper.class);
    private static final MessageMapper messageMapper = SqlSessionUtils.getSqlSession().getMapper(MessageMapper.class);
    private static final ClassroomMapper classroomMapper = SqlSessionUtils.getSqlSession().getMapper(ClassroomMapper.class);
    private static final Gson gson = new Gson();
    private Session session;
    private Integer userId;
    private Integer classId;
    private Integer role;
    private EndpointUser endpointUser;
    private SignInCounter signInCounter = null;

    private static void broadcastAll(Integer classId, String content) throws IOException {
        for (ChatEndpoint chatEndpoint : classroomStudentsMap.get(classId).values()) {
            chatEndpoint.session.getBasicRemote().sendText(content);
        }
        for (ChatEndpoint chatEndpoint : classroomTeachersMap.get(classId).values()) {
            chatEndpoint.session.getBasicRemote().sendText(content);
        }
    }

    private static void broadcastTeachers(Integer classId, String content) throws IOException {
        for (ChatEndpoint chatEndpoint : classroomTeachersMap.get(classId).values()) {
            chatEndpoint.session.getBasicRemote().sendText(content);
        }
    }

    public static void countDown(Integer classId) throws IOException {
        signInCounterMap.put(classId, signInCounterMap.get(classId) - 1);
        // 向指定班级的用户广播剩余签到时间
        broadcastAll(classId, gson.toJson(WsMsgWrapper.countDownMessage(getTImeLeft(classId))));
        if (signInCounterMap.get(classId) == 0) { // 签到结束通知所有班级成员
            broadcastAll(classId, gson.toJson(WsMsgWrapper.signInStatus(0)));
        }
    }

    private static String getTImeLeft(Integer classId) {
        int minutesLeft = signInCounterMap.get(classId) / 60;
        int secondsLeft = signInCounterMap.get(classId) % 60;
        return minutesLeft + " 分 " + secondsLeft + " 秒 ";
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String msg, Session session) throws IOException, SchedulerException, InterruptedException {
        WsMsg wsMsg = gson.fromJson(msg, WsMsg.class);
        String msgType = wsMsg.getType();
        switch (msgType) {
            case "VERIFY":  // 用户进入直播教室需要先验证身份
                String token = (String) wsMsg.getData();
                // 解析token
                this.userId = Integer.parseInt(JwtUtils.parseJwt(token));
                User user = userMapper.findById(userId);
                this.endpointUser = userMapper.getEndpointUserByUserId(userId);
                if (user == null) {
                    throw new RuntimeException();
                }
                this.session.getBasicRemote().sendText(gson.toJson(WsMsgWrapper.verifySuccessMsg()));
                break;
            case "CLASS":   // 用户刚进入直播教室时前端需传递班级号
                this.classId = Integer.parseInt((String) wsMsg.getData());
                // 如果班级不出现在Map中, 则初始化
                signInMap.computeIfAbsent(this.classId, k -> new HashMap<>());
                classroomStudentsMap.computeIfAbsent(this.classId, k -> new HashMap<>());
                classroomTeachersMap.computeIfAbsent(this.classId, k -> new HashMap<>());
                classroomNameListMap.computeIfAbsent(this.classId, k -> new HashMap<>());
                // 验证用户身份, 1-教师 0-学生 并加入班级在线用户名单
                LiveVerificationBody result = classroomMapper.verifyTeachingInfo(this.userId, this.classId);
                this.role = result == null ? 0 : 1;
                if (role == 1) {
                    // 如果是该班级教师, 则加入该班级的教师终端列表
                    classroomTeachersMap.get(this.classId).put(this.userId, this);
                    this.endpointUser.setRole("教师");
                } else {
                    // 如果是该班级学生, 则加入该班级的学生终端列表
                    classroomStudentsMap.get(this.classId).put(this.userId, this);
                    this.endpointUser.setRole("学生");
                }
                classroomNameListMap.get(this.classId).put(this.userId, this.endpointUser);
                // 向新加入直播教室的同学发送聊天历史信息
                List<Message> history = messageMapper.getMessageListByClassId(this.classId);
                this.session.getBasicRemote().sendText(gson.toJson(WsMsgWrapper.historyMsg(history)));
                // 向同班同学和老师广播在线用户名单
                broadcastAll(this.classId, gson.toJson(WsMsgWrapper.studentListMsg(new ArrayList<>(classroomNameListMap.get(this.classId).values()))));
                break;
            case "TEXT":    // 用户在聊天板块中发布消息
                // 将消息封装为Message对象
                Message msgObject = new Message();
                String text = (String) wsMsg.getData();
                msgObject.setSender(this.endpointUser.getName());
                msgObject.setText(text);
                msgObject.setToclass(this.classId);
                msgObject.setTime(new Date());
                // 将消息放入数据库
                messageMapper.insertMessage(msgObject);
                // 向同班同学和老师广播消息
                broadcastAll(this.classId, gson.toJson(WsMsgWrapper.textMsg(msgObject)));
                break;
            case "SIGN_IN_STATUS":  // 用户请求签到状态 2: 签到完成 1: 正在签到 0: 签到结束或未开始
                int status;
                if (signInMap.get(this.classId).containsKey(this.userId)) {
                    status = 2;
                } else {
                    status = signInCounterMap.getOrDefault(this.classId, 0) > 0 ? 1 : 0;
                }
                this.session.getBasicRemote().sendText(gson.toJson(WsMsgWrapper.signInStatus(status)));
                break;
            case "TIME_LEFT":   // 用户请求剩余签到时间
                this.session.getBasicRemote().sendText(gson.toJson(WsMsgWrapper.countDownMessage(getTImeLeft(this.classId))));
                break;
            case "SIGN_IN_START":   // 教师开启签到
                int duration = Integer.parseInt((String) wsMsg.getData());
                signInCounterMap.put(this.classId, duration * 60);
                if (this.signInCounter != null) {
                    this.signInCounter.stop();
                }
                this.signInCounter = new SignInCounter(this.classId, duration);
                // 重置签到表
                signInMap.put(this.classId, new HashMap<>());
                this.signInCounter.execute();
                this.session.getBasicRemote().sendText(gson.toJson(WsMsgWrapper.signInStatus(1)));
                broadcastTeachers(this.classId, gson.toJson(WsMsgWrapper.signInNameListMsg(new ArrayList<>(signInMap.get(this.classId).values()))));
                break;
            case "SIGN_IN": // 学生进行签到
                // 加入本班级的签到名单
                signInMap.get(this.classId).put(this.userId, this.endpointUser);
                // 向教师广播已签到同学名单
                broadcastTeachers(this.classId, gson.toJson(WsMsgWrapper.signInNameListMsg(new ArrayList<>(signInMap.get(this.classId).values()))));
                // 向签到同学发送签到情况
                this.session.getBasicRemote().sendText(gson.toJson(WsMsgWrapper.signInStatus(2)));
                break;
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        if (this.role == 1) {
            classroomTeachersMap.get(this.classId).remove(this.userId);
        } else {
            classroomStudentsMap.get(this.classId).remove(this.userId);
        }
        // 向同班同学和老师广播在线用户名单
        broadcastAll(this.classId, gson.toJson(WsMsgWrapper.studentListMsg(new ArrayList<>(classroomNameListMap.get(this.classId).values()))));
    }
}
