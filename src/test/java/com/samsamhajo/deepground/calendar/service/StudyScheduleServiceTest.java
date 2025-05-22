package com.samsamhajo.deepground.calendar.service;

import com.samsamhajo.deepground.calendar.dto.StudyScheduleRequestDto;
import com.samsamhajo.deepground.calendar.dto.StudyScheduleResponseDto;
import com.samsamhajo.deepground.calendar.entity.StudySchedule;
import com.samsamhajo.deepground.calendar.exception.ScheduleErrorCode;
import com.samsamhajo.deepground.calendar.exception.ScheduleException;
import com.samsamhajo.deepground.calendar.repository.StudyScheduleRepository;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudyScheduleServiceTest {

    @Mock
    private StudyScheduleRepository studyScheduleRepository;

    @Mock
    private StudyGroupRepository studyGroupRepository;

    @InjectMocks
    private StudyScheduleService studyScheduleService;

    private StudyGroup studyGroup;
    private StudyScheduleRequestDto requestDto;

    @BeforeEach
    void setup() {
        studyGroup = mock(StudyGroup.class);
        requestDto = StudyScheduleRequestDto.builder()
                .title("스터디 스케줄 1")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .description("스터디 설명")
                .location("온라인")
                .build();
    }

    @Test
    void createStudySchedule_Success() {
        // given
        when(studyGroupRepository.findById(anyLong())).thenReturn(Optional.of(studyGroup));
        when(studyScheduleRepository.existsByStudyGroupAndEndTimeGreaterThanAndStartTimeLessThan(studyGroup, requestDto.getStartTime(), requestDto.getEndTime())).thenReturn(false);

        when(studyScheduleRepository.save(any(StudySchedule.class)))
                .thenAnswer(invocation -> {
                    StudySchedule schedule = invocation.getArgument(0);

                    Field idField = StudySchedule.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(schedule, 1L);

                    return schedule;
                });

        // when
        StudyScheduleResponseDto responseDto = studyScheduleService.createStudySchedule(1L, requestDto);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(responseDto.getStartTime()).isEqualTo(requestDto.getStartTime());
        assertThat(responseDto.getEndTime()).isEqualTo(requestDto.getEndTime());
        assertThat(responseDto.getDescription()).isEqualTo(requestDto.getDescription());
        assertThat(responseDto.getLocation()).isEqualTo(requestDto.getLocation());

        verify(studyGroupRepository, times(1)).findById(anyLong());
        verify(studyScheduleRepository, times(1)).existsByStudyGroupAndEndTimeGreaterThanAndStartTimeLessThan(
                studyGroup,
                requestDto.getStartTime(),
                requestDto.getEndTime()
        );
        verify(studyScheduleRepository, times(1)).save(any(StudySchedule.class));
    }

    @Test
    void createStudySchedule_Fail_EndTimeBeforeStartTime() {
        // given
        when(studyGroupRepository.findById(anyLong())).thenReturn(Optional.of(studyGroup));

        LocalDateTime startTime = LocalDateTime.of(2025, 5, 20, 15, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 5, 20, 13, 0);

        requestDto = requestDto.toBuilder()
                .startTime(startTime)
                .endTime(endTime)
                .build();

        // when & then
        assertThatThrownBy(() -> studyScheduleService.createStudySchedule(1L, requestDto))
                .isInstanceOf(ScheduleException.class)
                .hasMessageContaining(ScheduleErrorCode.INVALID_DATE_RANGE.getMessage());

        verify(studyScheduleRepository, never()).save(any(StudySchedule.class));
    }

    @Test
    void createStudySchedule_Fail_StudyGroupNotFound() {
        // given
        when(studyGroupRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> studyScheduleService.createStudySchedule(1L, requestDto))
                .isInstanceOf(ScheduleException.class)
                .hasMessageContaining(ScheduleErrorCode.NOT_FOUND_SCHEDULE.getMessage());

        verify(studyScheduleRepository, never()).save(any(StudySchedule.class));
    }

    @Test
    void createStudySchedule_Fail_DuplicateSchedule() {
        // given
        when(studyGroupRepository.findById(anyLong())).thenReturn(Optional.of(studyGroup));
        when(studyScheduleRepository.existsByStudyGroupAndEndTimeGreaterThanAndStartTimeLessThan(
                studyGroup,
                requestDto.getStartTime(),
                requestDto.getEndTime()
        )).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> studyScheduleService.createStudySchedule(1L, requestDto))
                .isInstanceOf(ScheduleException.class)
                .hasMessageContaining(ScheduleErrorCode.DUPLICATE_SCHEDULE.getMessage());

        verify(studyScheduleRepository, never()).save(any(StudySchedule.class));
    }

    @Test
    void deleteStudySchedule_Success() {
        // given
        Long scheduleId = 1L;
        StudySchedule schedule = mock(StudySchedule.class);
        when(studyScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));

        // when
        studyScheduleService.deleteStudySchedule(scheduleId);

        // then
        verify(studyScheduleRepository).delete(schedule);
    }

    @Test
    void deleteStudySchedule_Fail_StudyScheduleNotFound() {
        // given
        Long scheduleId = 1L;
        when(studyScheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> studyScheduleService.deleteStudySchedule(scheduleId))
                .isInstanceOf(ScheduleException.class)
                .hasMessageContaining(ScheduleErrorCode.NOT_FOUND_SCHEDULE.getMessage());

        verify(studyScheduleRepository, never()).delete(any());
    }
}