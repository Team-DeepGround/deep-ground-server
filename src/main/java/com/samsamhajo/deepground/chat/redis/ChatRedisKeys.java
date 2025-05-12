package com.samsamhajo.deepground.chat.redis;

public final class ChatRedisKeys {
    private static final String LATEST_MESSAGE = "chatrooms:%s:latest_message_time";

    public static String getLatestMessageKey(Long roomId) {
        return String.format(LATEST_MESSAGE, roomId);
    }
}
