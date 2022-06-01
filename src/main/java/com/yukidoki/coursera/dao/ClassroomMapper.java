package com.yukidoki.coursera.dao;

import com.yukidoki.coursera.entity.Classroom;

import java.util.List;

public interface ClassroomMapper {
    Integer insertClassroom(Classroom classroom);

    Integer getStudentNumByClassId(Integer id);

    Integer getStudentListByClassId(Integer id);
}
