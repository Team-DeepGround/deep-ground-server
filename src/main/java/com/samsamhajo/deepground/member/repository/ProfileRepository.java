package com.samsamhajo.deepground.member.repository;


import com.samsamhajo.deepground.member.entity.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<MemberProfile, Long> {

    Optional<MemberProfile> findByMemberId(Long memberId);

    Optional<MemberProfile> findByProfilePublicId(UUID profilePublicId);
}
