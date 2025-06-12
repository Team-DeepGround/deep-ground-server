package com.samsamhajo.deepground.member.repository;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
@RequiredArgsConstructor
public class MemberPresenceRepositoryImpl implements MemberPresenceRepository {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "PRESENCE:";

    @Override
    public void save(Long memberId, Duration timeout) {
        String redisKey = generateKey(memberId);
        redisTemplate.opsForValue().increment(redisKey);
        redisTemplate.expire(redisKey, timeout);
    }

    @Override
    public boolean existsById(Long memberId) {
        String redisKey = generateKey(memberId);
        return redisTemplate.hasKey(redisKey);
    }

    @Override
    public List<Boolean> findPresentMembers(List<Long> memberIds) {
        if (CollectionUtils.isEmpty(memberIds)) {
            return Collections.emptyList();
        }

        List<String> redisKeys = memberIds.stream()
                .map(this::generateKey)
                .toList();

        return redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (String redisKey : redisKeys) {
                connection.keyCommands().exists(redisKey.getBytes());
            }
            return null;
        }).stream().map(Boolean.TRUE::equals).toList();
    }

    @Override
    public void deleteById(Long memberId) {
        String redisKey = generateKey(memberId);
        Long count = redisTemplate.opsForValue().decrement(redisKey);

        if (count != null && count <= 0) {
            redisTemplate.delete(redisKey);
        }
    }

    private String generateKey(Long memberId) {
        return KEY_PREFIX + memberId;
    }
}
