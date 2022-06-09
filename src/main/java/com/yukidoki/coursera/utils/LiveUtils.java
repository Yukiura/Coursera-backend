package com.yukidoki.coursera.utils;

import com.google.gson.Gson;
import com.yukidoki.coursera.dao.ClassroomMapper;
import com.yukidoki.coursera.dao.UserMapper;
import com.yukidoki.coursera.entity.LiveVerificationBody;
import com.yukidoki.coursera.entity.LoginUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.Set;

public class LiveUtils {
    private final Gson gson;
    private final ClassroomMapper classroomMapper;
    private final UserMapper userMapper;

    public LiveUtils() {
        this.gson = new Gson();
        this.classroomMapper = SqlSessionUtils.getSqlSession().getMapper(ClassroomMapper.class);
        this.userMapper = SqlSessionUtils.getSqlSession().getMapper(UserMapper.class);
    }

    public String liveTokenGenerate(Integer cid) {
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LiveVerificationBody body = new LiveVerificationBody(user.getId(), cid);
        return JwtUtils.getJwt(gson.toJson(body));
    }

    public Boolean liveTokenVerification(String classId, String token) {
        LiveVerificationBody body = gson.fromJson(JwtUtils.parseJwt(token), LiveVerificationBody.class);
        if(!body.getCid().toString().equals(classId)) return false;
        LiveVerificationBody result;
        Set<String> roleSet = new HashSet<>(userMapper.getRoleListByUserId(body.getUid()));
        if (roleSet.contains("学生")) {
            result = classroomMapper.verifySelectionInfo(body.getUid(), body.getCid());
        } else if (roleSet.contains("教师")) {
            result = classroomMapper.verifyTeachingInfo(body.getUid(), body.getCid());
        } else {
            return false;
        }
        return result != null;
    }
}
