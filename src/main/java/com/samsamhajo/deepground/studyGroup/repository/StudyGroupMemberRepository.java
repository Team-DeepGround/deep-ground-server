package com.samsamhajo.deepground.studyGroup.repository;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupMemberRepository extends JpaRepository<StudyGroupMember, Long> {
  boolean existsByMemberAndStudyGroup(Member member, StudyGroup studyGroup);

  Optional<StudyGroupMember> findByMemberAndStudyGroup(Member member, StudyGroup studyGroup);

  @Query("""
  SELECT m FROM StudyGroupMember m
  JOIN FETCH m.studyGroup sg
  WHERE m.member.id = :memberId
    AND m.isAllowed = true
    AND sg.creator.id <> :memberId
  ORDER BY sg.createdAt DESC
""")
  List<StudyGroupMember> findAllByMemberIdAndIsAllowedTrueAndNotCreator(@Param("memberId") Long memberId);

  List<StudyGroupMember> findAllByStudyGroupIdAndIsAllowedTrue(Long studyGroupId);

  @Query("SELECT m FROM StudyGroupMember m JOIN FETCH m.member WHERE m.studyGroup.id = :studyGroupId")
  List<StudyGroupMember> findAllWithMemberByStudyGroupId(@Param("studyGroupId") Long studyGroupId);

  @Query("SELECT m FROM StudyGroupMember m JOIN FETCH m.member WHERE m.studyGroup.id = :studyGroupId AND m.isAllowed = true ORDER BY m.createdAt ASC")
  List<StudyGroupMember> findAcceptedMembersWithInfo(@Param("studyGroupId") Long studyGroupId);

  int countByStudyGroup_IdAndIsAllowedTrue(Long studyGroupId);

  int countByStudyGroup_IdAndIsAllowedFalse(Long studyGroupId);

  Optional<StudyGroupMember> findByStudyGroupIdAndMemberId(Long studyGroupId, Long memberId);

  void deleteByStudyGroupIdAndMemberId(Long studyGroupId, Long memberId);
}
