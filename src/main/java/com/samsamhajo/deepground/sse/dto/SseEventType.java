package com.samsamhajo.deepground.sse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SseEventType {
    CONNECTED("connected"),
    HEARTBEAT("heartbeat"),
    NOTIFICATION("notification"),
    UNREAD_COUNT("unreadCount");

    private final String name;
}
