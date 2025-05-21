package com.samsamhajo.deepground.chat.redis;

import com.samsamhajo.deepground.external.redis.RedisKey;

public final class ChatRedisKeys {
    private static final String LATEST_MESSAGE = "chatrooms:%s:latest_message_time";
    private static final String CHATROOM_MEMBER = "chatrooms:%s:members";

    public static RedisKey getLatestMessageKey(Long roomId) {
        return RedisKey.of(LATEST_MESSAGE, roomId);
    }

    public static RedisKey getChatRoomMemberKey(Long chatRoomId) {
        return RedisKey.of(CHATROOM_MEMBER, chatRoomId);
    }
}
