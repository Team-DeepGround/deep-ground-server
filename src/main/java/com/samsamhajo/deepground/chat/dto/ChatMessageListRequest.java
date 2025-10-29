package com.samsamhajo.deepground.chat.dto;

import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class ChatMessageListRequest {

    private static final int DEFAULT_LIMIT = 20;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final LocalDateTime cursor;

    @Min(value = 1, message = "조회 개수는 최소 1개 이상이어야 합니다.")
    private final Integer limit;

    public ChatMessageListRequest(LocalDateTime cursor, Integer limit) {
        this.cursor = cursor;
        this.limit = limit == null ? DEFAULT_LIMIT : limit;
    }
}
