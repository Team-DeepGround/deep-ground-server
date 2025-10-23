package com.samsamhajo.deepground.studyGroup.repository;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import java.util.List;
import java.util.Optional;

import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMemberStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupMemberRepository extends JpaRepository<StudyGroupMember, Long> {

  @Query("""
  SELECT m FROM StudyGroupMember m
  JOIN FETCH m.studyGroup sg
  WHERE m.member.id = :memberId
    AND m.studyGroupMemberStatus =:status
    AND sg.creator.id <> :memberId
    AND sg.deleted = false
  ORDER BY sg.createdAt DESC
""")
  List<StudyGroupMember> findAllByMemberIdAndStudyGroupMemberStatusAndNotCreator(@Param("memberId") Long memberId,@Param("status") StudyGroupMemberStatus status);

  @Query("SELECT sgm " +
          "FROM StudyGroupMember sgm " +
          "JOIN FETCH sgm.member m " +
          "JOIN FETCH m.memberProfile pf " +
          "WHERE sgm.studyGroup.id = :studyGroupId AND sgm.studyGroupMemberStatus = :status")
  List<StudyGroupMember> findAllByStudyGroupIdAndStudyGroupMemberStatusForPending(@Param("studyGroupId") Long studyGroupId, @Param("status") StudyGroupMemberStatus status);

  List<StudyGroupMember> findAllByStudyGroupIdAndStudyGroupMemberStatus(@Param("studyGroupId") Long studyGroupId, @Param("status") StudyGroupMemberStatus status);

  @Query("SELECT m FROM StudyGroupMember m JOIN FETCH m.member WHERE m.studyGroup.id = :studyGroupId AND m.deleted = false")
  List<StudyGroupMember> findAllWithMemberByStudyGroupId(@Param("studyGroupId") Long studyGroupId);

  @Query("SELECT m FROM StudyGroupMember m JOIN FETCH m.member WHERE m.studyGroup.id = :studyGroupId AND m.studyGroupMemberStatus =:status AND m.deleted = false ORDER BY m.createdAt ASC")
  List<StudyGroupMember> findAcceptedMembersWithInfo(@Param("studyGroupId") Long studyGroupId,@Param("status") StudyGroupMemberStatus status);

  int countByStudyGroup_IdAndStudyGroupMemberStatus(Long studyGroupId,@Param("status")StudyGroupMemberStatus status);

  boolean existsByMemberAndStudyGroupAndStudyGroupMemberStatusIn(Member member, StudyGroup studyGroup, List<StudyGroupMemberStatus> statuses);

  Optional<StudyGroupMember> findByStudyGroupIdAndMemberIdAndDeletedFalse(@NotNull(message = "스터디 그룹 ID는 필수입니다.") Long studyGroupId, @NotNull(message = "강퇴할 멤버 ID는 필수입니다.") Long targetMemberId);

}
