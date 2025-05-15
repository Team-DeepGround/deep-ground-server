package com.samsamhajo.deepground.studyGroup.repository;


import com.samsamhajo.deepground.studyGroup.entity.GroupStatus;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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

}
