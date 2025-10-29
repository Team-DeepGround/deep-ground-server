package com.samsamhajo.deepground.chat.dto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import lombok.Getter;

@Getter
public class ReadMessageResponse {

    private final Long memberId;
    private final ZonedDateTime lastReadMessageTime;

    private ReadMessageResponse(Long memberId, ZonedDateTime lastReadMessageTime) {
        this.memberId = memberId;
        this.lastReadMessageTime = lastReadMessageTime;
    }

    public static ReadMessageResponse of(Long memberId, ZonedDateTime lastReadMessageTime) {
        return new ReadMessageResponse(memberId, lastReadMessageTime);
    }
}
