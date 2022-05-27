package com.yukidoki.coursera.dao;

import com.yukidoki.coursera.entity.CourseCard;

public interface CourseCardMapper {
    CourseCard getCourseCardByClassId(Integer id);
}
