package com.yukidoki.coursera.dao;

import com.yukidoki.coursera.entity.Classroom;
import com.yukidoki.coursera.entity.Student;

import java.util.List;

public interface ClassroomMapper {
    Integer insertClassroom(Classroom classroom);

    Integer getStudentNumByClassId(Integer id);

    List<Student> getStudentListByClassId(Integer id);

    List<String> getClassListByUserId(Integer id);
}
