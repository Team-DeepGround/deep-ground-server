package com.samsamhajo.deepground.chat.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomInfo {

    private Long chatRoomId;
    private String name;
    private LocalDateTime lastReadMessageTime;
    private long memberCount;
}
