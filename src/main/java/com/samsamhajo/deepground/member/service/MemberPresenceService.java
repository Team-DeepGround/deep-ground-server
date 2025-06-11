package com.samsamhajo.deepground.member.service;

import com.samsamhajo.deepground.member.repository.MemberPresenceRepository;
import com.samsamhajo.deepground.sse.event.SseSubscribeEvent;
import com.samsamhajo.deepground.sse.event.SseUnsubscribeEvent;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberPresenceService {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(30);

    private final MemberPresenceRepository memberPresenceRepository;

    @EventListener
    public void handleSseSubscribeEvent(SseSubscribeEvent event) {
        memberPresenceRepository.save(event.getMemberId(), DEFAULT_TIMEOUT);
    }

    @EventListener
    public void handleSseUnsubscribeEvent(SseUnsubscribeEvent event) {
        memberPresenceRepository.deleteById(event.getMemberId());
    }

    public boolean isOnlineMember(Long memberId) {
        return memberPresenceRepository.existsById(memberId);
    }

    public List<Boolean> isOnlineMembers(List<Long> memberIds) {
        return memberPresenceRepository.findPresentMembers(memberIds);
    }
}
