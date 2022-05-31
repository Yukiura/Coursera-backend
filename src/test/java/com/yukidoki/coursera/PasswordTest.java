package com.yukidoki.coursera;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class PasswordTest {

    @Test
    public void testPasswordEncoder() {
        String t = "as:as sd:sd ff:ss";
        List<String> ts = new ArrayList<>(Arrays.asList(t.split(" ")));
        System.out.println(ts);
    }
}
