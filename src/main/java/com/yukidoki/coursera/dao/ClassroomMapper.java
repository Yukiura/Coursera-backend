package com.yukidoki.coursera.dao;

import com.yukidoki.coursera.entity.Classroom;
import com.yukidoki.coursera.entity.LiveVerificationBody;
import com.yukidoki.coursera.entity.Student;

import java.util.List;

public interface ClassroomMapper {
    Integer insertClassroom(Classroom classroom);

    Integer getStudentNumByClassId(Integer id);

    List<Student> getStudentListByClassId(Integer id);

    List<String> getClassListByUserId(Integer id);

    LiveVerificationBody verifySelectionInfo(Integer uid, Integer cid);

    LiveVerificationBody verifyTeachingInfo(Integer uid, Integer cid);

    List<Integer> getSelectionListByUserId(Integer uid);

    List<Integer> getTeachingListByUserId(Integer uid);

    Integer getClassroomLiveById(Integer id);

    Integer setClassroomLiveById(Integer id, Integer status);

    List<Classroom> getClassroomListByCourseId(Integer cid);

    Integer select(Integer uid, Integer cid);
}
