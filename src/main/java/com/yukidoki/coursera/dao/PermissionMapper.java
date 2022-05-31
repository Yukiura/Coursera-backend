package com.yukidoki.coursera.dao;

import java.util.List;

public interface PermissionMapper {
    List<String> getPermissionListByUserId(int id);
}
