package com.samsamhajo.deepground.studyGroup.repository;

import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupMemberRepository extends JpaRepository<StudyGroupMember, Long> {
}
