package com.yukidoki.coursera.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

import static io.jsonwebtoken.security.Keys.secretKeyFor;

public class JwtUtils {
    // 有效期
    private static final Long JWT_TTL = 60 * 60 * 1000L;
    // 密钥
    private static final SecretKey SECRET_KEY = secretKeyFor(SignatureAlgorithm.HS256);

    private static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private static String create(String id, String subject, Long ttlMillis) {
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
                .setIssuer("yukidoki")
                .setIssuedAt(now)
                .signWith(SECRET_KEY)
                .setExpiration(expDate)
                .compact();
    }

    public static String getJwt(String id, String subject, Long ttlMillis) {
        return create(id, subject, ttlMillis);
    }

    public static String getJwt(String subject, Long ttlMillis) {
        return create(getUUID(), subject, ttlMillis);
    }

    public static String getJwt(String subject) {
        return create(getUUID(), subject, null);
    }

    public static String parseJwt(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }
}
