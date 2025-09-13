package com.samsamhajo.deepground.calendar.repository;

import com.samsamhajo.deepground.calendar.entity.StudySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Long> {

    boolean existsByStudyGroupIdAndEndTimeGreaterThanAndStartTimeLessThan(Long studyGroupId, LocalDateTime startTime, LocalDateTime endTime);

    List<StudySchedule> findAllByStudyGroupId(Long studyGroupId);
}
