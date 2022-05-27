package com.yukidoki.coursera.dao;

import com.yukidoki.coursera.entity.Kyc;

import java.util.List;

public interface KycMapper {
    List<Kyc> getStudentListByClassCode(String code);

    Integer insertKyc(Kyc kyc);
}
