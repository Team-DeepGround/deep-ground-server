package com.samsamhajo.deepground.sse.dto;

import lombok.Getter;

@Getter
public class SseEvent {

    private final Long memberId;
    private final SseEventType type;
    private final Object data;

    private SseEvent(Long memberId, SseEventType type, Object data) {
        this.memberId = memberId;
        this.type = type;
        this.data = data;
    }

    public static SseEvent of(SseEventType type, Object data) {
        return new SseEvent(null, type, data);
    }

    public static SseEvent of(Long memberId, SseEventType type, Object data) {
        return new SseEvent(memberId, type, data);
    }

    public String getName() {
        return type.getName();
    }
}
