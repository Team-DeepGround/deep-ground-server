package com.samsamhajo.deepground.studyGroup.repository;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupMemberRepository extends JpaRepository<StudyGroupMember, Long> {
  boolean existsByMemberAndStudyGroup(Member member, StudyGroup studyGroup);

  Optional<StudyGroupMember> findByMemberAndStudyGroup(Member member, StudyGroup studyGroup);

  List<StudyGroupMember> findAllByMemberIdAndIsAllowedTrueOrderByStudyGroupCreatedAtDesc(Long memberId);

  List<StudyGroupMember> findAllByStudyGroupIdAndIsAllowedTrue(Long studyGroupId);
}
