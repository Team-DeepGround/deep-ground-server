package com.samsamhajo.deepground.sse.service;

import com.samsamhajo.deepground.sse.dto.SseEvent;
import com.samsamhajo.deepground.sse.dto.SseEventType;
import com.samsamhajo.deepground.sse.event.SseSubscribeEvent;
import com.samsamhajo.deepground.sse.event.SseUnsubscribeEvent;
import com.samsamhajo.deepground.sse.repository.SseEmitterRepository;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseEmitterService {

    private static final Long DEFAULT_TIMEOUT = 30 * 60 * 1000L; // 30분
    private static final SseEvent CONNECTED_EVENT = SseEvent.of(SseEventType.CONNECTED, "sse connected");
    private static final SseEvent HEARTBEAT_EVENT = SseEvent.of(SseEventType.HEARTBEAT, "ping");

    private final SseEmitterRepository sseEmitterRepository;
    private final ApplicationEventPublisher eventPublisher;

    public SseEmitter subscribe(Long memberId) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        sseEmitterRepository.save(memberId, sseEmitter);

        sseEmitter.onCompletion(() -> unsubscribe(memberId, sseEmitter));
        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitter.onError(sseEmitter::completeWithError);

        eventPublisher.publishEvent(new SseSubscribeEvent(this, memberId));
        send(List.of(sseEmitter), CONNECTED_EVENT);
        return sseEmitter;
    }

    public void broadcast(Long memberId, SseEvent event) {
        List<SseEmitter> emitters = sseEmitterRepository.findAllById(memberId);
        send(emitters, event);
    }

    @Scheduled(fixedRate = 30000) // 30초
    private void sendHeartbeatToAll() {
        List<SseEmitter> emitters = sseEmitterRepository.findAll();
        send(emitters, HEARTBEAT_EVENT);
    }

    private void send(List<SseEmitter> emitters, SseEvent event) {
        emitters.forEach(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event()
                        .name(event.getName())
                        .data(event.getData()));
            } catch (IOException e) {
                sseEmitter.complete();
            }
        });
    }

    private void unsubscribe(Long memberId, SseEmitter sseEmitter) {
        eventPublisher.publishEvent(new SseUnsubscribeEvent(this, memberId));
        sseEmitterRepository.delete(memberId, sseEmitter);
    }
}
