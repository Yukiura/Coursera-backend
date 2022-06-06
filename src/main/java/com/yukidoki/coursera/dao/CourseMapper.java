package com.yukidoki.coursera.dao;

import com.yukidoki.coursera.entity.Course;
import com.yukidoki.coursera.entity.SearchResult;

import java.util.List;

public interface CourseMapper {
    Integer insertCourse(Course course);

    List<SearchResult> getSearchResultListByCourseName(String name);
}
