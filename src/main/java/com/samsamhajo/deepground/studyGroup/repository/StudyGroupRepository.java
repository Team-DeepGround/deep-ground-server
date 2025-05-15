package com.samsamhajo.deepground.studyGroup.repository;

import com.samsamhajo.deepground.studyGroup.entity.GroupStatus;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

  Page<StudyGroup> findByGroupStatusAndTitleContainingIgnoreCaseOrGroupStatusAndExplanationContainingIgnoreCase(
      GroupStatus status1, String titleKeyword,
      GroupStatus status2, String explanationKeyword,
      Pageable pageable
  );

  Page<StudyGroup> findByTitleContainingIgnoreCaseOrExplanationContainingIgnoreCase(
      String titleKeyword, String explanationKeyword,
      Pageable pageable
  );

  Page<StudyGroup> findByGroupStatus(
      GroupStatus groupStatus,
      Pageable pageable
  );
  
  @Query("SELECT sg FROM StudyGroup sg " +
      "LEFT JOIN FETCH sg.member " +
      "LEFT JOIN FETCH sg.members " +
      "LEFT JOIN FETCH sg.comments " +
      "WHERE sg.id = :id")
  Optional<StudyGroup> findWithMemberAndCommentsById(@Param("id") Long studyGroupId);

}
