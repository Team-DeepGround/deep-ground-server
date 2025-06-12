package com.samsamhajo.deepground.sse.dto;

import lombok.Getter;

@Getter
public class SseEvent {

    private final SseEventType type;
    private final Object data;

    private SseEvent(SseEventType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public static SseEvent of(SseEventType type, Object data) {
        return new SseEvent(type, data);
    }

    public String getName() {
        return type.getName();
    }
}
