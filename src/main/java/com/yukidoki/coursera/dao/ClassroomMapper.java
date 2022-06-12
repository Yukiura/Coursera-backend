package com.yukidoki.coursera.dao;

import com.yukidoki.coursera.entity.Classroom;
import com.yukidoki.coursera.entity.LiveVerificationBody;

import java.util.List;

public interface ClassroomMapper {
    Integer getStudentNumByClassId(Integer id);

    LiveVerificationBody verifySelectionInfo(Integer uid, Integer cid);

    LiveVerificationBody verifyTeachingInfo(Integer uid, Integer cid);

    List<Integer> getSelectionListByUserId(Integer uid);

    List<Integer> getTeachingListByUserId(Integer uid);

    Integer getClassroomLiveById(Integer id);

    void setClassroomLiveById(Integer id, Integer status);

    List<Classroom> getClassroomListByCourseId(Integer cid);

    Integer select(Integer uid, Integer cid);
}
