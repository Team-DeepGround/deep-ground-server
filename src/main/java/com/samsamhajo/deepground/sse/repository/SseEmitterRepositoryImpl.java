package com.samsamhajo.deepground.sse.repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class SseEmitterRepositoryImpl implements SseEmitterRepository {

    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(Long memberId, SseEmitter sseEmitter) {
        emitters.computeIfAbsent(memberId, k -> new CopyOnWriteArrayList<>())
                .add(sseEmitter);
        return sseEmitter;
    }

    @Override
    public List<SseEmitter> findAllById(Long memberId) {
        return emitters.getOrDefault(memberId, Collections.emptyList());
    }

    @Override
    public List<SseEmitter> findAll() {
        return emitters.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
    }

    @Override
    public void delete(Long memberId, SseEmitter sseEmitter) {
        List<SseEmitter> memberEmitters = emitters.get(memberId);

        if (!CollectionUtils.isEmpty(memberEmitters)) {
            memberEmitters.remove(sseEmitter);

            if (memberEmitters.isEmpty()) {
                emitters.remove(memberId);
            }
        }
    }
}
