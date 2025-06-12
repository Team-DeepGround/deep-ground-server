package com.samsamhajo.deepground.sse.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.samsamhajo.deepground.sse.dto.SseEvent;
import com.samsamhajo.deepground.sse.dto.SseEventType;
import com.samsamhajo.deepground.sse.repository.SseEmitterRepository;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@ExtendWith(MockitoExtension.class)
class SseEmitterServiceTest {

    @InjectMocks
    private SseEmitterService sseEmitterService;

    @Mock
    private SseEmitterRepository sseEmitterRepository;

    private final Long memberId = 1L;
    private SseEmitter sseEmitter;

    @BeforeEach
    void setUp() {
        sseEmitter = mock(SseEmitter.class);
    }

    @Test
    @DisplayName("SSE 구독에 성공한다")
    void subscribe_success() {
        // given
        when(sseEmitterRepository.save(eq(memberId), any(SseEmitter.class))).thenReturn(sseEmitter);

        // when
        SseEmitter result = sseEmitterService.subscribe(memberId);

        // then
        verify(sseEmitterRepository).save(eq(memberId), any(SseEmitter.class));
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("이벤트 브로드캐스트를 성공한다")
    void broadcast_success() throws IOException {
        // given
        List<SseEmitter> emitters = Collections.singletonList(sseEmitter);
        SseEvent event = SseEvent.of(SseEventType.NOTIFICATION, "message");
        when(sseEmitterRepository.findAllById(memberId)).thenReturn(emitters);

        // when
        sseEmitterService.broadcast(memberId, event);

        // then
        verify(sseEmitterRepository).findAllById(memberId);
        verify(sseEmitter, times(1)).send(any(SseEmitter.SseEventBuilder.class));
    }
}
