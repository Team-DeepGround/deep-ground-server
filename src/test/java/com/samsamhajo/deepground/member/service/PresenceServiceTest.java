package com.samsamhajo.deepground.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.samsamhajo.deepground.friend.service.FriendService;
import com.samsamhajo.deepground.member.Dto.PresenceDto;
import com.samsamhajo.deepground.member.repository.PresenceRepository;
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
class PresenceServiceTest {

    @InjectMocks
    private PresenceService presenceService;

    @Mock
    private FriendService friendService;

    @Mock
    private PresenceRepository presenceRepository;

    private final Long memberId = 1L;

    @Test
    @DisplayName("SSE 구독 이벤트가 발행되면 온라인 상태를 저장한다")
    void handleSseSubscribeEvent_success() {
        // given
        SseSubscribeEvent event = new SseSubscribeEvent(memberId);

        // when
        presenceService.handleSseSubscribeEvent(event);

        // then
        verify(presenceRepository, times(1)).save(eq(memberId), any(Duration.class));
    }

    @Test
    @DisplayName("SSE 구독 취소 이벤트가 발행되면 온라인 상태를 삭제한다")
    void handleSseUnsubscribeEvent_success() {
        // given
        SseUnsubscribeEvent event = new SseUnsubscribeEvent(memberId);

        // when
        presenceService.handleSseUnsubscribeEvent(event);

        // then
        verify(presenceRepository, times(1)).deleteById(eq(memberId));
    }

    @Test
    @DisplayName("단일 멤버 온라인 여부 확인에 성공한다")
    void isOnlineMember_success() {
        // given
        when(presenceRepository.existsById(memberId)).thenReturn(true);

        // when
        boolean online = presenceService.isOnlineMember(memberId);

        // then
        assertThat(online).isTrue();
        verify(presenceRepository, times(1)).existsById(eq(memberId));
    }

    @Test
    @DisplayName("친구 온라인 여부 확인에 성공한다")
    void isOnlineMembers_success() {
        // given
        List<Long> friendIds = List.of(1L, 2L, 3L);
        when(friendService.getFriendMemberIdByMemberId(memberId)).thenReturn(friendIds);
        when(presenceRepository.findOnlineMembers(friendIds))
                .thenReturn(List.of(true, false, true));

        // when
        List<PresenceDto> onlineMembers = presenceService.isOnlineMembers(memberId);

        // then
        assertThat(onlineMembers).isNotNull();
        assertThat(onlineMembers).hasSize(friendIds.size());
        verify(presenceRepository, times(1)).findOnlineMembers(eq(friendIds));
    }
}
