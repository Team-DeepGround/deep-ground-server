package com.samsamhajo.deepground.sse.service;

import com.samsamhajo.deepground.sse.dto.SseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SseEventListener {

    private final SseEmitterService sseEmitterService;

    @EventListener
    public void handleSseEvent(SseEvent sseEvent) {
        sseEmitterService.broadcast(sseEvent);
    }
}
