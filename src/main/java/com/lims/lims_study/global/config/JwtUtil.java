package com.lims.lims_study.global.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // JWT의 subject를 사용자 이름으로 설정
                .setIssuedAt(new Date()) // 토큰 발급 시간 설정
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS512, secret) // 서명 알고리즘과 비밀 키 설정
                .compact();
    }


    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret) // 서명 검증을 위한 비밀 키 설정
                .parseClaimsJws(token) // JWT 파싱 및 검증
                .getBody(); // 토큰의 본문(Claims) 가져오기

        return claims.getSubject(); // JWT에서 subject(사용자 이름) 추출
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token); // JWT의 유효성을 검증
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}