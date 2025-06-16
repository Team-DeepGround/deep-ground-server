package com.samsamhajo.deepground.member.repository;

import java.time.Duration;
import java.util.List;

public interface MemberPresenceRepository {

    void save(Long memberId, Duration timeout);

    boolean existsById(Long memberId);

    List<Boolean> findPresentMembers(List<Long> memberIds);

    void deleteById(Long memberId);
}
