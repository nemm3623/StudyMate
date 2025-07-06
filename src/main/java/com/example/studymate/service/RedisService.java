package com.example.studymate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    // Redis 서버와 통신할 수 있게하는 스프링 클래스
    private final RedisTemplate<String, String> redisTemplate;

    public void set(String key, String value, long timeoutSeconds) {
        // opsForValue -> 문자열을 처리하는 Redis 명령어
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(timeoutSeconds));
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }
}

