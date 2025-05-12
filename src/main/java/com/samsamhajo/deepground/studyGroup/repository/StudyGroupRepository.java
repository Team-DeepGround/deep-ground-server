package com.samsamhajo.deepground.studyGroup.repository;

import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

  @Query("SELECT sg FROM StudyGroup sg " +
      "LEFT JOIN FETCH sg.member " +
      "LEFT JOIN FETCH sg.members " +
      "LEFT JOIN FETCH sg.comments " +
      "WHERE sg.id = :id")
  Optional<StudyGroup> findWithMemberAndCommentsById(@Param("id") Long studyGroupId);

}
