package com.samsamhajo.deepground.chat.dto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomInfo {

    private Long id;
    private Long chatRoomId;
    private String name;
    private ZonedDateTime lastReadMessageTime;
    private long memberCount;
}
