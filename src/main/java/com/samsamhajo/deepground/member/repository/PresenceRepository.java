package com.samsamhajo.deepground.member.repository;

import java.time.Duration;
import java.util.List;

public interface PresenceRepository {

    void save(Long memberId, Duration timeout);

    boolean existsById(Long memberId);

    List<Boolean> findOnlineMembers(List<Long> memberIds);

    void deleteById(Long memberId);
}
