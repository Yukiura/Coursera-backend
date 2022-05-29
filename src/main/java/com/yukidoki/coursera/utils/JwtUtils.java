package com.yukidoki.coursera.utils;

import com.google.gson.Gson;
import com.yukidoki.coursera.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

import static io.jsonwebtoken.security.Keys.secretKeyFor;

public class JwtUtils {
    // 有效期
    public static final Long JWT_TTL = 60 * 60 * 1000L;
    // 密钥
    public static final SecretKey SECRET_KEY = secretKeyFor(SignatureAlgorithm.HS256);

    public static String create(String id, String subject, Long ttlMillis) {
        var nowMillis = System.currentTimeMillis();
        var now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtils.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(id)
                .setSubject(subject)
                .setIssuer("sg")
                .setIssuedAt(now)
                .signWith(SECRET_KEY)
                .setExpiration(expDate)
                .compact();
    }

    public static String parse(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

    public static void main(String[] args) {
        var gson = new Gson();
        var user = new User();
        user.setUsername("Jade");
        user.setId(10);
        user.setBio("Test.");
        String subject = gson.toJson(user);
        String jwt = JwtUtils.create(UUID.randomUUID().toString(), subject, null);
        System.out.println(jwt);
        System.out.println(parse(jwt));
    }
}
