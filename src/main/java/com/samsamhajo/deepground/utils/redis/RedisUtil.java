package com.samsamhajo.deepground.utils.redis;

import java.time.Duration;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    public static final Duration LONG_TTL = Duration.ofDays(7);

    private final RedisTemplate<String, Object> redisTemplate;

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public <T> T getAndCache(String key, Supplier<T> fallback) {
        return getAndCache(key, fallback, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAndCache(String key, Supplier<T> fallback, Duration timeout) {
        T cached = (T) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

        T value = fallback.get();
        if (value != null) {
            set(key, value, timeout);
        }
        return value;
    }

    public void set(String key, Object value) {
        set(key, value, null);
    }

    public void set(String key, Object value, Duration ttl) {
        if (ttl == null) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            redisTemplate.opsForValue().set(key, value, ttl);
        }
    }

    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }
}
