package com.yukidoki.coursera.controller;

import com.yukidoki.coursera.dao.ClassroomMapper;
import com.yukidoki.coursera.utils.SqlSessionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/classroom")
public class ClassroomController {
    private final ClassroomMapper classroomMapper = SqlSessionUtils.getSqlSession().getMapper(ClassroomMapper.class);

    @GetMapping("/student/num/{classId}")
    public Integer getStudentNumByClassId(@PathVariable("classId") Integer classId) {
        return classroomMapper.getStudentNumByClassId(classId);
    }
}
