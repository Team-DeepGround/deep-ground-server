package com.samsamhajo.deepground.email.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String KEY_PREFIX = "EMAIL-VERIFICATION:";

    public void save(String email, String code, long timeoutSeconds) {
        String key = generateKey(email);
        redisTemplate.opsForValue().set(key, code, timeoutSeconds, TimeUnit.SECONDS);
    }

    public String getCode(String email) {
        String key = generateKey(email);
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String email) {
        String key = generateKey(email);
        redisTemplate.delete(key);
    }

    public boolean exists(String email) {
        String key = generateKey(email);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    private String generateKey(String email) {
        return KEY_PREFIX + email;
    }
}
