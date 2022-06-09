package com.yukidoki.coursera.controller;

import com.yukidoki.coursera.dao.ClassroomMapper;
import com.yukidoki.coursera.live.callbackbean.CallBackDataOnConnect;
import com.yukidoki.coursera.live.callbackbean.CallBackDataOnPlay;
import com.yukidoki.coursera.live.callbackbean.CallBackDataOnPublish;
import com.yukidoki.coursera.live.callbackbean.CallBackDataOnUnpublish;
import com.yukidoki.coursera.utils.LiveUtils;
import com.yukidoki.coursera.utils.SqlSessionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/live")
public class SRSCallBackController {
    ClassroomMapper classroomMapper = SqlSessionUtils.getSqlSession().getMapper(ClassroomMapper.class);

    @ResponseBody
    @RequestMapping("/connect")
    public int onConnect(@RequestBody CallBackDataOnConnect data) {
        System.out.println("收到连接请求");
        System.out.println("APP: " + data.getApp());
        return "live".equals(data.getApp()) ? 0 : 1;
    }

    @ResponseBody
    @RequestMapping("/close")
    public int onClose(@RequestBody String data) {
        return 0;
    }

    @ResponseBody
    @RequestMapping("/publish")
    public int onPublish(@RequestBody CallBackDataOnPublish data) {
        System.out.println("收到推流请求");
        if (!"live".equals(data.getApp())) return 1;
        String token = data.getParam();
        token = token.substring(1);
        String classId = data.getStream();
        LiveUtils liveUtils = new LiveUtils();
        System.out.println("推流TOKEN: " + token);
        System.out.println("推流教室: " + classId);
        classroomMapper.setClassroomLiveById(Integer.parseInt(classId), 1);
        return liveUtils.liveTokenVerification(classId, token) ? 0 : 1;
    }

    @ResponseBody
    @RequestMapping("/unpublish")
    public int onUnpublish(@RequestBody CallBackDataOnUnpublish data) {
        String classId = data.getStream();
        classroomMapper.setClassroomLiveById(Integer.parseInt(classId), 0);
        return 0;
    }

    @ResponseBody
    @RequestMapping("/play")
    public int onPlay(@RequestBody CallBackDataOnPlay data) {
        System.out.println("收到拉流请求");
        if (!"live".equals(data.getApp())) return 1;
        String token = data.getParam();
        token = token.substring(1);
        String classId = data.getStream();
        LiveUtils liveUtils = new LiveUtils();
        System.out.println("拉流TOKEN: " + token);
        System.out.println("拉流教室: " + classId);
        return liveUtils.liveTokenVerification(classId, token) ? 0 : 1;
    }

    @ResponseBody
    @RequestMapping("/stop")
    public int onStop(@RequestBody String data) {
        return 0;
    }
}
