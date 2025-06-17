package com.samsamhajo.deepground.chat.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponse {

    private Long chatRoomId;
    private String name;
    private LocalDateTime lastReadMessageTime;
    private Long unreadCount;

    public static ChatRoomResponse of(ChatRoomInfo info, Long unreadCount) {
        return ChatRoomResponse.builder()
                .chatRoomId(info.getChatRoomId())
                .name(info.getName())
                .lastReadMessageTime(info.getLastReadMessageTime())
                .unreadCount(unreadCount)
                .build();
    }
}
