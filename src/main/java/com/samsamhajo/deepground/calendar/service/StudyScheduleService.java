package com.samsamhajo.deepground.calendar.service;

import com.samsamhajo.deepground.calendar.dto.StudyScheduleRequestDto;
import com.samsamhajo.deepground.calendar.dto.StudyScheduleResponseDto;
import com.samsamhajo.deepground.calendar.entity.StudySchedule;
import com.samsamhajo.deepground.calendar.repository.StudyScheduleRepository;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyScheduleService {

    private final StudyScheduleRepository studyScheduleRepository;
    private final StudyGroupRepository studyGroupRepository;

    @Transactional
    public StudyScheduleResponseDto createStudySchedule(Long studyGroupId, StudyScheduleRequestDto requestDto) {

        StudyGroup studyGroup = validateSchedule(studyGroupId, requestDto);

        StudySchedule studySchedule = StudySchedule.of(
                studyGroup,
                requestDto.getTitle(),
                requestDto.getStartTime(),
                requestDto.getEndTime(),
                requestDto.getDescription(),
                requestDto.getLocation()
        );

        StudySchedule savedSchedule = studyScheduleRepository.save(studySchedule);

        return StudyScheduleResponseDto.builder()
                .id(savedSchedule.getId())
                .title(savedSchedule.getTitle())
                .startTime(savedSchedule.getStartTime())
                .endTime(savedSchedule.getEndTime())
                .description(savedSchedule.getDescription())
                .location(savedSchedule.getLocation())
                .build();

    }

    private StudyGroup validateSchedule(Long studyGroupId, StudyScheduleRequestDto requestDto) {

        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new EntityNotFoundException("스터디 그룹이 존재하지 않습니다."));

        if (requestDto.getEndTime().isBefore(requestDto.getStartTime())) {
            throw new IllegalArgumentException("종료 시간이 시작 시간보다 늦을 수 없습니다.");
        }

        boolean isDuplicated = studyScheduleRepository.existsByStudyGroupAndEndTimeGreaterThanAndStartTimeLessThan(
                studyGroup,
                requestDto.getStartTime(),
                requestDto.getEndTime()
        );

        if (isDuplicated) {
            throw new IllegalStateException("해당 시간에 이미 스터디 스케줄이 존재합니다.");
        }

        return studyGroup;
    }
}
