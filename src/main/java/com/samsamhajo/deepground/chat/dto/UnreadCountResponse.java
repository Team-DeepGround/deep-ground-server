package com.samsamhajo.deepground.chat.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UnreadCountResponse {

    private final Long chatRoomId;
    private final Long unreadCount;
    private final LocalDateTime latestMessageTime;

    private UnreadCountResponse(Long chatRoomId, Long unreadCount, LocalDateTime latestMessageTime) {
        this.chatRoomId = chatRoomId;
        this.unreadCount = unreadCount;
        this.latestMessageTime = latestMessageTime;
    }

    public static UnreadCountResponse of(Long chatroomId, Long unreadCount, LocalDateTime latestMessageTime) {
        return new UnreadCountResponse(chatroomId, unreadCount, latestMessageTime);
    }
}
