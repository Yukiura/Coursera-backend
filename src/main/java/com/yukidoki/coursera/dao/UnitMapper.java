package com.yukidoki.coursera.dao;

import com.yukidoki.coursera.entity.Unit;

import java.util.List;

public interface UnitMapper {
    Integer insertUnit(Unit unit);

    List<String> getUnitList();

    Integer getUnitIdByName(String name);
}
