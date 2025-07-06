package com.example.studymate.security;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final Key key;  // 서명을 위한 키
    
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    // 토큰 생성 메소드
    public String createAccessToken(String username) {
        long accessTokenValidity = 1000L * 60 * 15; // 15분
        return Jwts.builder().setSubject(username)  // 토큰 주인 설정
                .setIssuedAt(new Date())            // 토큰 생성 시각
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))  // 토큰 유효시간
                .signWith(key, SignatureAlgorithm.HS256)    // 토큰 서명
                .compact(); // 토큰 생성
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)         // 대칭키 설정
                    .build()                    // parser 객체 생성
                    .parseClaimsJws(token);     // 만료시간, 토큰 구조, 서명까지 모두 검증
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // username 추출
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String createRefreshToken(String username) {
        long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7;   // 7일
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer "))
            return bearer.substring(7);

        return null;
    }

}
