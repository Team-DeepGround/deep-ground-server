package com.samsamhajo.deepground.studyGroup.repository;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.entity.GroupStatus;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

  @Query("""
    SELECT DISTINCT sg FROM StudyGroup sg
    LEFT JOIN FETCH sg.creator
    LEFT JOIN sg.techTags tag
    WHERE (:status IS NULL OR sg.groupStatus = :status)
      AND (
          (:keyword IS NULL OR LOWER(sg.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
          OR (:keyword IS NULL OR LOWER(sg.explanation) LIKE LOWER(CONCAT('%', :keyword, '%')))
      )
      AND (:tags IS NULL OR tag IN :tags)
""")
  Page<StudyGroup> searchWithFilters(
      @Param("status") GroupStatus status,
      @Param("keyword") String keyword,
      @Param("tags") List<String> tags,
      Pageable pageable
  );

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
      "LEFT JOIN FETCH sg.creator " +
      "LEFT JOIN FETCH sg.members " +
      "LEFT JOIN FETCH sg.comments " +
      "WHERE sg.id = :id")
  Optional<StudyGroup> findWithCreatorAndCommentsById(@Param("id") Long studyGroupId);

  List<StudyGroup> findAllByCreator_IdOrderByCreatedAtDesc(Long memberId);

}
