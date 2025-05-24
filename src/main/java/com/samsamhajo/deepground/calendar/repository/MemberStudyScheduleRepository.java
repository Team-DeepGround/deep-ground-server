package com.samsamhajo.deepground.calendar.repository;

import com.samsamhajo.deepground.calendar.entity.MemberStudySchedule;
import com.samsamhajo.deepground.calendar.entity.StudySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberStudyScheduleRepository extends JpaRepository<MemberStudySchedule, Long> {

    void deleteAllByStudySchedule(StudySchedule schedule);
}
