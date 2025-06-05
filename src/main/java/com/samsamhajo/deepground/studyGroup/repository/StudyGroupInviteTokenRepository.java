package com.samsamhajo.deepground.studyGroup.repository;

import com.samsamhajo.deepground.studyGroup.entity.StudyGroupInviteToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupInviteTokenRepository extends JpaRepository<StudyGroupInviteToken, Long> {
  boolean existsByStudyGroup_IdAndEmail(Long studyGroupId, String email);
  Optional<StudyGroupInviteToken> findByToken(String token);
}
