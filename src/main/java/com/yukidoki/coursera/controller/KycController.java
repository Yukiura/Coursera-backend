package com.yukidoki.coursera.controller;

import com.yukidoki.coursera.dao.KycMapper;
import com.yukidoki.coursera.entity.Kyc;
import com.yukidoki.coursera.utils.SqlSessionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kyc")
public class KycController {
    private final KycMapper kycMapper = SqlSessionUtils.getSqlSession().getMapper(KycMapper.class);

    @GetMapping("/class/{code}")
    public List<Kyc> findKycListByClassCode(@PathVariable("code") String code) {
        return kycMapper.getStudentListByClassCode(code);
    }

    @PostMapping("/new")
    public Integer newKyc(Kyc kyc) {
        return kycMapper.insertKyc(kyc);
    }
}
