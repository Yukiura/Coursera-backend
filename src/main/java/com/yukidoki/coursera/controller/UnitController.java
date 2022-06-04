package com.yukidoki.coursera.controller;

import com.yukidoki.coursera.dao.UnitMapper;
import com.yukidoki.coursera.entity.Unit;
import com.yukidoki.coursera.utils.SqlSessionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/unit")
public class UnitController {
    private final UnitMapper unitMapper = SqlSessionUtils.getSqlSession().getMapper(UnitMapper.class);

    @GetMapping("/all")
    public List<String> getUnitList() {
        return unitMapper.getUnitList();
    }
}
