package com.samsamhajo.deepground.chat.redis;

import com.samsamhajo.deepground.external.redis.RedisKey;

public final class ChatRedisKeys {
    private static final String LATEST_MESSAGE = "chatrooms:%s:latest_message_time";

    public static RedisKey getLatestMessageKey(Long roomId) {
        return RedisKey.of(LATEST_MESSAGE, roomId);
    }
}
