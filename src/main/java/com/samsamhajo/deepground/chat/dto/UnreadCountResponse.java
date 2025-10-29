package com.samsamhajo.deepground.chat.dto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import lombok.Getter;

@Getter
public class UnreadCountResponse {

    private final Long chatRoomId;
    private final Long unreadCount;
    private final ZonedDateTime latestMessageTime;

    private UnreadCountResponse(Long chatRoomId, Long unreadCount, ZonedDateTime latestMessageTime) {
        this.chatRoomId = chatRoomId;
        this.unreadCount = unreadCount;
        this.latestMessageTime = latestMessageTime;
    }

    public static UnreadCountResponse of(Long chatroomId, Long unreadCount, ZonedDateTime latestMessageTime) {
        return new UnreadCountResponse(chatroomId, unreadCount, latestMessageTime);
    }
}
