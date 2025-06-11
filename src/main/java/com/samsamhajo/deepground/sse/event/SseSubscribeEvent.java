package com.samsamhajo.deepground.sse.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SseSubscribeEvent extends ApplicationEvent {

    private final Long memberId;

    public SseSubscribeEvent(Object source, Long memberId) {
        super(source);
        this.memberId = memberId;
    }
}
