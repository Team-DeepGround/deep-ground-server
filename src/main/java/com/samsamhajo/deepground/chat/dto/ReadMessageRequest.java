package com.samsamhajo.deepground.chat.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadMessageRequest {

    private LocalDateTime lastReadMessageTime;
}
