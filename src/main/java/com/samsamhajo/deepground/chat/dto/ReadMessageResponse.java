package com.samsamhajo.deepground.chat.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReadMessageResponse {

    private final Long memberId;
    private final LocalDateTime lastReadMessageTime;

    private ReadMessageResponse(Long memberId, LocalDateTime lastReadMessageTime) {
        this.memberId = memberId;
        this.lastReadMessageTime = lastReadMessageTime;
    }

    public static ReadMessageResponse of(Long memberId, LocalDateTime lastReadMessageTime) {
        return new ReadMessageResponse(memberId, lastReadMessageTime);
    }
}
