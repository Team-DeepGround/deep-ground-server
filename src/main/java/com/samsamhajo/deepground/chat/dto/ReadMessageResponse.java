package com.samsamhajo.deepground.chat.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;

@Getter
public class ReadMessageResponse {

    private final UUID publicId;
    private final LocalDateTime lastReadMessageTime;

    private ReadMessageResponse(UUID publicId, LocalDateTime lastReadMessageTime) {
        this.publicId = publicId;
        this.lastReadMessageTime = lastReadMessageTime;
    }

    public static ReadMessageResponse of(UUID publicId, LocalDateTime lastReadMessageTime) {
        return new ReadMessageResponse(publicId, lastReadMessageTime);
    }
}
