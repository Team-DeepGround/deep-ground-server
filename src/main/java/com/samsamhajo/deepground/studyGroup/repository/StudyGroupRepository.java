package com.samsamhajo.deepground.studyGroup.repository;

import com.samsamhajo.deepground.studyGroup.entity.GroupStatus;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupReply;
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
  LEFT JOIN FETCH sg.studyGroupTechTags sgt
  LEFT JOIN FETCH sgt.techStack ts
  WHERE (:status IS NULL OR sg.groupStatus = :status)
    AND (
        (:keyword IS NULL OR LOWER(sg.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
        OR (:keyword IS NULL OR LOWER(sg.explanation) LIKE LOWER(CONCAT('%', :keyword, '%')))
    )
    AND (:stackNames IS NULL OR ts.name IN :stackNames)
    AND (:onOffline IS NULL OR
        (:onOffline = 'ONLINE' AND sg.isOffline = false) OR
        (:onOffline = 'OFFLINE' AND sg.isOffline = true) OR
        (:onOffline = 'ALL'))
""")
  Page<StudyGroup> searchWithFilters(
      @Param("status") GroupStatus status,
      @Param("keyword") String keyword,
      @Param("stackNames") List<String> stackNames,
      @Param("onOffline") String onOffline,
      Pageable pageable
  );

  @Query("""
  SELECT DISTINCT sg FROM StudyGroup sg
  LEFT JOIN FETCH sg.creator
  LEFT JOIN FETCH sg.members
  LEFT JOIN FETCH sg.comments c
  LEFT JOIN FETCH c.member
  LEFT JOIN FETCH sg.studyGroupTechTags sgt
  LEFT JOIN FETCH sgt.techStack ts
  WHERE sg.id = :id
""")
  Optional<StudyGroup> findWithCreatorAndCommentsById(@Param("id") Long id);

  @Query("""
  SELECT r FROM StudyGroupReply r
  JOIN FETCH r.member
  WHERE r.comment.id IN :commentIds
""")
  List<StudyGroupReply> findRepliesByCommentIds(@Param("commentIds") List<Long> commentIds);

  List<StudyGroup> findAllByCreator_IdOrderByCreatedAtDesc(Long memberId);

    List<StudyGroup> findByIdIn(List<Long> studyGroupIds);
}
