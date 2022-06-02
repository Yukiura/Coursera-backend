package com.yukidoki.coursera.controller;

import com.yukidoki.coursera.dao.ClassroomMapper;
import com.yukidoki.coursera.entity.Student;
import com.yukidoki.coursera.utils.SqlSessionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/classroom")
public class ClassroomController {
    private final ClassroomMapper classroomMapper = SqlSessionUtils.getSqlSession().getMapper(ClassroomMapper.class);

    @GetMapping("/student/num/{classId}")
    @PreAuthorize("hasAuthority('class:info:num')")
    public Integer getStudentNumByClassId(@PathVariable("classId") Integer classId) {
        return classroomMapper.getStudentNumByClassId(classId);
    }

    @GetMapping("/student/list/{classId}")
    @PreAuthorize("hasAuthority('class:info:studentList')")
    public List<Student> getStudentListByClassId(@PathVariable("classId") Integer classId) {
        return classroomMapper.getStudentListByClassId(classId);
    }
}
