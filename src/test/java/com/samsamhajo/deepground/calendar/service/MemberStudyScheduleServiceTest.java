package com.samsamhajo.deepground.calendar.service;

import com.samsamhajo.deepground.calendar.dto.MemberStudyScheduleRequestDto;
import com.samsamhajo.deepground.calendar.dto.MemberStudyScheduleResponseDto;
import com.samsamhajo.deepground.calendar.entity.MemberStudySchedule;
import com.samsamhajo.deepground.calendar.entity.StudySchedule;
import com.samsamhajo.deepground.calendar.exception.ScheduleErrorCode;
import com.samsamhajo.deepground.calendar.exception.ScheduleException;
import com.samsamhajo.deepground.calendar.repository.MemberStudyScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MemberStudyScheduleServiceTest {

    @Mock
    private MemberStudyScheduleRepository memberStudyScheduleRepository;

    @InjectMocks
    private MemberStudyScheduleService memberStudyScheduleService;

    @Test
    @DisplayName("참석 선택 + 중요 일정 체크/메모 추가 성공")
    void update_MemberStudySchedule_Success() {
        // given
        Long memberStudyScheduleId = 1L;

        StudySchedule studySchedule = mock(StudySchedule.class);
        MemberStudySchedule memberStudySchedule = MemberStudySchedule.of(studySchedule, false, false, null);

        MemberStudyScheduleRequestDto requestDto = MemberStudyScheduleRequestDto.builder()
                .isAvailable(true)
                .isImportant(true)
                .memo("memo")
                .build();

        when(memberStudyScheduleRepository.findById(memberStudyScheduleId))
                .thenReturn(Optional.of(memberStudySchedule));

        // when
        MemberStudyScheduleResponseDto response = memberStudyScheduleService.update(memberStudyScheduleId, requestDto);

        // then
        assertThat(response.getId()).isEqualTo(memberStudySchedule.getId());
        assertThat(response.getIsAvailable()).isTrue();
        assertThat(response.getIsImportant()).isTrue();
        assertThat(response.getMemo()).isEqualTo("memo");
    }

    @Test
    @DisplayName("업데이트 실패 - 존재하지 않는 스케줄 id")
    void updateMemberStudySchedule_fail_scheduleNotFound() {
        // given
        Long invalidMemberStudyScheduleId = 1L;

        MemberStudyScheduleRequestDto requestDto = MemberStudyScheduleRequestDto.builder()
                .isAvailable(true)
                .isImportant(true)
                .memo("memo")
                .build();

        when(memberStudyScheduleRepository.findById(invalidMemberStudyScheduleId))
                .thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> memberStudyScheduleService.update(invalidMemberStudyScheduleId, requestDto))
                .isInstanceOf(ScheduleException.class)
                .hasMessageContaining(ScheduleErrorCode.SCHEDULE_NOT_FOUND.getMessage());
    }
}
