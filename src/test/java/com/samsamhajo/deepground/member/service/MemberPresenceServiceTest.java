package com.samsamhajo.deepground.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.samsamhajo.deepground.member.repository.MemberPresenceRepository;
import com.samsamhajo.deepground.sse.event.SseSubscribeEvent;
import com.samsamhajo.deepground.sse.event.SseUnsubscribeEvent;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberPresenceServiceTest {

    @InjectMocks
    private MemberPresenceService memberPresenceService;

    @Mock
    private MemberPresenceRepository memberPresenceRepository;

    private final Long memberId = 1L;

    @Test
    @DisplayName("SSE 구독 이벤트가 발행되면 온라인 상태를 저장한다")
    void handleSseSubscribeEvent_success() {
        // given
        SseSubscribeEvent event = new SseSubscribeEvent(this, memberId);

        // when
        memberPresenceService.handleSseSubscribeEvent(event);

        // then
        verify(memberPresenceRepository, times(1)).save(eq(memberId), any(Duration.class));
    }

    @Test
    @DisplayName("SSE 구독 취소 이벤트가 발행되면 온라인 상태를 삭제한다")
    void handleSseUnsubscribeEvent_success() {
        // given
        SseUnsubscribeEvent event = new SseUnsubscribeEvent(this, memberId);

        // when
        memberPresenceService.handleSseUnsubscribeEvent(event);

        // then
        verify(memberPresenceRepository, times(1)).deleteById(eq(memberId));
    }

    @Test
    @DisplayName("단일 멤버 온라인 여부 확인에 성공한다")
    void isOnlineMember_success() {
        // given
        when(memberPresenceRepository.existsById(memberId)).thenReturn(true);

        // when
        boolean online = memberPresenceService.isOnlineMember(memberId);

        // then
        assertThat(online).isTrue();
        verify(memberPresenceRepository, times(1)).existsById(eq(memberId));
    }

    @Test
    @DisplayName("여러 멤버 온라인 여부 확인에 성공한다")
    void isOnlineMembers_success() {
        // given
        List<Long> memberIds = List.of(1L, 2L, 3L);
        when(memberPresenceRepository.findPresentMembers(memberIds))
                .thenReturn(List.of(true, false, true));

        // when
        List<Boolean> onlineMembers = memberPresenceService.isOnlineMembers(memberIds);

        // then
        assertThat(onlineMembers).isNotNull();
        assertThat(onlineMembers).hasSize(memberIds.size());
        verify(memberPresenceRepository, times(1)).findPresentMembers(eq(memberIds));
    }
}
