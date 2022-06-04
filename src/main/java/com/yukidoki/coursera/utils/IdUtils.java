package com.yukidoki.coursera.utils;

import java.util.UUID;

public class IdUtils {
    public static int generate(int start) {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        int id = start;
        for (int i = 0; i < 5; i++) {
            char c = uuid.charAt(i);
            int diff;
            if (c >= '0' && c <= '9')
                diff = c - '0';
            else
                diff = c - 'a';
            id *= 10;
            id += diff;
        }
        return id;
    }
}
