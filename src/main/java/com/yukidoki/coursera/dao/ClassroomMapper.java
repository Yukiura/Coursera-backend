package com.yukidoki.coursera.dao;

import com.yukidoki.coursera.entity.Classroom;

public interface ClassroomMapper {
    Integer insertClassroom(Classroom classroom);

    Integer getStudentNumByClassId(Integer id);
}
