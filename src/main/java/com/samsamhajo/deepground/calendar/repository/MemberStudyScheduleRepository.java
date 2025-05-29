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

    @Query("SELECT mss FROM MemberStudySchedule mss JOIN FETCH mss.studySchedule WHERE mss.member.id = :memberId")
    List<MemberStudySchedule> findAllByMemberId(@Param("memberId") Long memberId);
}
