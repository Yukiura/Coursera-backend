package com.yukidoki.coursera.controller;

import com.yukidoki.coursera.dao.CourseCardMapper;
import com.yukidoki.coursera.dao.CourseMapper;
import com.yukidoki.coursera.entity.CourseCard;
import com.yukidoki.coursera.entity.SearchResult;
import com.yukidoki.coursera.utils.SqlSessionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseCardMapper courseCardMapper = SqlSessionUtils.getSqlSession().getMapper(CourseCardMapper.class);
    private final CourseMapper courseMapper = SqlSessionUtils.getSqlSession().getMapper(CourseMapper.class);

    @GetMapping("/card/{classId}")
    @PreAuthorize("hasAuthority('course:info:card')")
    public CourseCard getCourseCardByClassId(@PathVariable("classId") Integer classId) {
        return courseCardMapper.getCourseCardByClassId(classId);
    }

    @GetMapping("/search")
    public List<SearchResult> search(@RequestParam("name") String name) {
        return courseMapper.getSearchResultListByCourseName(name);
    }
}
