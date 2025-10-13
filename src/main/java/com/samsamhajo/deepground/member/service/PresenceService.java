package com.samsamhajo.deepground.member.service;

import com.samsamhajo.deepground.friend.service.FriendService;
import com.samsamhajo.deepground.member.repository.PresenceRepository;
import com.samsamhajo.deepground.member.Dto.PresenceDto;
import com.samsamhajo.deepground.sse.dto.SseEvent;
import com.samsamhajo.deepground.sse.dto.SseEventType;
import com.samsamhajo.deepground.sse.event.SseSubscribeEvent;
import com.samsamhajo.deepground.sse.event.SseUnsubscribeEvent;
import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PresenceService {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(30);

    private final FriendService friendService;
    private final PresenceRepository presenceRepository;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener
    public void handleSseSubscribeEvent(SseSubscribeEvent event) {
        presenceRepository.save(event.memberId(), DEFAULT_TIMEOUT);
        publishPresenceEvent(event.memberId(), true);
    }

    @EventListener
    public void handleSseUnsubscribeEvent(SseUnsubscribeEvent event) {
        presenceRepository.deleteById(event.memberId());
        publishPresenceEvent(event.memberId(), isOnlineMember(event.memberId()));
    }

    public boolean isOnlineMember(Long memberId) {
        return presenceRepository.existsById(memberId);
    }

    public List<PresenceDto> isOnlineMembers(Long memberId) {
        List<Long> friendIds = friendService.getFriendMemberIdByMemberId(memberId);

        List<Boolean> isOnline = presenceRepository.findOnlineMembers(friendIds);
        return IntStream.range(0, friendIds.size())
                .mapToObj(i -> new PresenceDto(friendIds.get(i), isOnline.get(i)))
                .toList();
    }

    private void publishPresenceEvent(Long memberId, boolean isOnline) {
        List<Long> friendIds = friendService.getFriendMemberIdByMemberId(memberId);
        friendIds.forEach(friendId -> {
            PresenceDto presence = new PresenceDto(memberId, isOnline);
            eventPublisher.publishEvent(SseEvent.of(friendId, SseEventType.PRESENCE, presence));
        });
    }
}
