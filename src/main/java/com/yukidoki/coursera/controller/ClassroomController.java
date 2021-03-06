package com.yukidoki.coursera.controller;

import com.yukidoki.coursera.dao.ClassroomMapper;
import com.yukidoki.coursera.dao.UserMapper;
import com.yukidoki.coursera.entity.Classroom;
import com.yukidoki.coursera.entity.LiveVerificationBody;
import com.yukidoki.coursera.entity.LoginUser;
import com.yukidoki.coursera.utils.SqlSessionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/classroom")
public class ClassroomController {
    private final ClassroomMapper classroomMapper = SqlSessionUtils.getSqlSession().getMapper(ClassroomMapper.class);
    private final UserMapper userMapper = SqlSessionUtils.getSqlSession().getMapper(UserMapper.class);

    @GetMapping("/student/num")
    @PreAuthorize("hasAuthority('class:info:num')")
    public Integer getStudentNumByClassId(@RequestParam("classId") Integer classId) {
        return classroomMapper.getStudentNumByClassId(classId);
    }

    @GetMapping("/list")
    public List<Integer> classIdList() {
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> roleSet = new HashSet<>(userMapper.getRoleListByUserId(user.getId()));
        if (roleSet.contains("学生")) {
            return classroomMapper.getSelectionListByUserId(user.getId());
        } else if (roleSet.contains("教师")) {
            return classroomMapper.getTeachingListByUserId(user.getId());
        } else {
            return null;
        }
    }

    @GetMapping("/status")
    public Integer status(@RequestParam("classId") Integer classId) {
        return classroomMapper.getClassroomLiveById(classId);
    }

    @GetMapping("/course")
    public List<Classroom> course(@RequestParam("courseId") Integer courseId) {
        return classroomMapper.getClassroomListByCourseId(courseId);
    }

    @GetMapping("/verify")
    public Boolean verify(@RequestParam("classroomId") Integer classroomId) {
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LiveVerificationBody result = classroomMapper.verifySelectionInfo(user.getId(), classroomId);
        return result != null;
    }

    @PostMapping("/select")
    public int select(@RequestParam("classroomId") Integer classroomId) {
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return classroomMapper.select(user.getId(), classroomId);
    }
}
