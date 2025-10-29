package com.samsamhajo.deepground.chat.dto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReadMessageRequest {

    private ZonedDateTime lastReadMessageTime;
}
