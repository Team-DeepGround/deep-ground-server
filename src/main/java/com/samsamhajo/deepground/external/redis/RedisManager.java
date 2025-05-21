package com.samsamhajo.deepground.external.redis;

import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class RedisManager {

    public static final Duration LONG_TTL = Duration.ofDays(7);

    private final RedisTemplate<String, Object> redisTemplate;

    public <T> T getValue(RedisKey key) {
        return (T) redisTemplate.opsForValue().get(key.toString());
    }

    public <T> T getValueAndCache(RedisKey key, Supplier<T> fallback) {
        return getValueAndCache(key, fallback, null);
    }

    public <T> T getValueAndCache(RedisKey key, Supplier<T> fallback, Duration ttl) {
        T cached = (T) redisTemplate.opsForValue().get(key.toString());
        if (cached != null) {
            return cached;
        }

        T value = fallback.get();
        if (value != null) {
            setValue(key, value, ttl);
        }
        return value;
    }

    public void setValue(RedisKey key, Object value) {
        setValue(key, value, null);
    }

    public void setValue(RedisKey key, Object value, Duration ttl) {
        if (ttl == null) {
            redisTemplate.opsForValue().set(key.toString(), value);
        } else {
            redisTemplate.opsForValue().set(key.toString(), value, ttl);
        }
    }

    public Long increment(RedisKey key) {
        return redisTemplate.opsForValue().increment(key.toString());
    }

    public <T> Set<T> getSet(RedisKey key) {
        return (Set<T>) redisTemplate.opsForSet().members(key.toString());
    }

    public <T> Set<T> getSetAndCache(RedisKey key, Supplier<List<T>> fallback) {
        return getSetAndCache(key, fallback, null);
    }

    public <T> Set<T> getSetAndCache(RedisKey key, Supplier<List<T>> fallback, Duration ttl) {
        if (hasKey(key)) {
            return getSet(key);
        }

        List<T> value = fallback.get();
        if (value == null || value.isEmpty()) {
            return Collections.emptySet();
        }
        addSet(key, value, ttl);
        return new HashSet<>(value);
    }

    public <T> void addSet(RedisKey key, List<T> value) {
        addSet(key, value, null);
    }

    public <T> void addSet(RedisKey key, List<T> value, Duration ttl) {
        T[] array = (T[]) value.toArray(new Object[0]);
        redisTemplate.opsForSet().add(key.toString(), array);
        expire(key, ttl);
    }

    public Boolean isMember(RedisKey key, Object o) {
        return redisTemplate.opsForSet().isMember(key.toString(), o);
    }

    public boolean hasKey(RedisKey key) {
        return redisTemplate.hasKey(key.toString());
    }

    public void expire(RedisKey key, Duration ttl) {
        if (ttl == null) {
            return;
        }
        redisTemplate.expire(key.toString(), ttl);
    }

    public boolean delete(RedisKey key) {
        return redisTemplate.delete(key.toString());
    }
}
