package com.samsamhajo.deepground.calendar.repository;

import com.samsamhajo.deepground.calendar.entity.MemberStudySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberStudyScheduleRepository extends JpaRepository<MemberStudySchedule, Long> {

    void deleteAllByStudyScheduleId(Long scheduleId);

    @Query("""
    SELECT mss
    FROM MemberStudySchedule mss
    JOIN FETCH mss.studySchedule ss
    JOIN FETCH ss.studyGroup sg
    JOIN StudyGroupMember sgm
      ON sgm.member.id = mss.member.id AND sgm.studyGroup.id = sg.id
    WHERE mss.member.id = :memberId
      AND sgm.isAllowed = true
      AND sgm.deleted = false
    """)
    List<MemberStudySchedule> findAllByMemberId(@Param("memberId") Long memberId);

    List<MemberStudySchedule> findByStudyScheduleId(Long studyScheduleId);
}
