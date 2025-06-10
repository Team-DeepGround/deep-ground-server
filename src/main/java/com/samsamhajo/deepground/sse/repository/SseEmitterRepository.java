package com.samsamhajo.deepground.sse.repository;

import java.util.List;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterRepository {

    SseEmitter save(Long memberId, SseEmitter sseEmitter);

    List<SseEmitter> findAllById(Long memberId);

    List<SseEmitter> findAll();

    void delete(Long memberId, SseEmitter sseEmitter);
}
