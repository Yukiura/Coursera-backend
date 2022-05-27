package com.yukidoki.coursera.controller;

import com.yukidoki.coursera.dao.CourseCardMapper;
import com.yukidoki.coursera.entity.CourseCard;
import com.yukidoki.coursera.utils.SqlSessionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseCardMapper courseCardMapper = SqlSessionUtils.getSqlSession().getMapper(CourseCardMapper.class);

    @GetMapping("/card/{classId}")
    public CourseCard getCourseCardByClassId(@PathVariable("classId") Integer classId) {
        return courseCardMapper.getCourseCardByClassId(classId);
    }
}
