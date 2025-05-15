package com.samsamhajo.deepground.external.redis;

import java.time.Duration;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisManager {

    public static final Duration LONG_TTL = Duration.ofDays(7);

    private final RedisTemplate<String, Object> redisTemplate;

    @SuppressWarnings("unchecked")
    public <T> T get(RedisKey key) {
        return (T) redisTemplate.opsForValue().get(key.toString());
    }

    public <T> T getAndCache(RedisKey key, Supplier<T> fallback) {
        return getAndCache(key, fallback, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAndCache(RedisKey key, Supplier<T> fallback, Duration timeout) {
        T cached = (T) redisTemplate.opsForValue().get(key.toString());
        if (cached != null) {
            return cached;
        }

        T value = fallback.get();
        if (value != null) {
            set(key, value, timeout);
        }
        return value;
    }

    public void set(RedisKey key, Object value) {
        set(key, value, null);
    }

    public void set(RedisKey key, Object value, Duration ttl) {
        if (ttl == null) {
            redisTemplate.opsForValue().set(key.toString(), value);
        } else {
            redisTemplate.opsForValue().set(key.toString(), value, ttl);
        }
    }

    public boolean delete(RedisKey key) {
        return redisTemplate.delete(key.toString());
    }
}
