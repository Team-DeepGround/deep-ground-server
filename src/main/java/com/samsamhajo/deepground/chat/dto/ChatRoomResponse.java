package com.samsamhajo.deepground.chat.dto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponse {

    private Long id;
    private Long chatRoomId;
    private String name;
    private ZonedDateTime lastReadMessageTime;
    private long memberCount;
    private Long unreadCount;

    public static ChatRoomResponse of(ChatRoomInfo info, Long unreadCount) {
        return ChatRoomResponse.builder()
                .id(info.getId())
                .chatRoomId(info.getChatRoomId())
                .name(info.getName())
                .lastReadMessageTime(info.getLastReadMessageTime())
                .memberCount(info.getMemberCount())
                .unreadCount(unreadCount)
                .build();
    }
}
