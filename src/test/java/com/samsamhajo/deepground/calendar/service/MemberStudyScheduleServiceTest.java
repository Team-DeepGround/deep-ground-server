package com.samsamhajo.deepground.calendar.service;

import com.samsamhajo.deepground.calendar.dto.MemberScheduleCalendarResponseDto;
import com.samsamhajo.deepground.calendar.dto.MemberStudyScheduleRequestDto;
import com.samsamhajo.deepground.calendar.dto.MemberStudyScheduleResponseDto;
import com.samsamhajo.deepground.calendar.entity.MemberStudySchedule;
import com.samsamhajo.deepground.calendar.entity.StudySchedule;
import com.samsamhajo.deepground.calendar.exception.ScheduleErrorCode;
import com.samsamhajo.deepground.calendar.exception.ScheduleException;
import com.samsamhajo.deepground.calendar.repository.MemberStudyScheduleRepository;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMemberStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.time.LocalDateTime;
import java.util.List;

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

    private final Long userId = 1L;

    @Test
    @DisplayName("로그인 한 사용자의 멤버 스터디 일정 조회 성공")
    void findMemberSchedulesByMemberId_Success() {
        // given
        Long studyScheduleId = 10L;
        Long studyGroupId = 100L;
        StudyGroup studyGroup = mock(StudyGroup.class);
        when(studyGroup.getId()).thenReturn(studyGroupId);

        StudySchedule studySchedule = mock(StudySchedule.class);
        when(studySchedule.getId()).thenReturn(studyScheduleId);
        when(studySchedule.getTitle()).thenReturn("스터디 일정 제목");
        when(studySchedule.getStartTime()).thenReturn(LocalDateTime.of(2025, 5, 30, 14, 0));
        when(studySchedule.getEndTime()).thenReturn(LocalDateTime.of(2025, 5, 30, 16, 0));
        when(studySchedule.getStudyGroup()).thenReturn(studyGroup);


        Member member = mock(Member.class);
        MemberStudySchedule memberStudySchedule = MemberStudySchedule.of(member, studySchedule, true, true, "memo");

        when(memberStudyScheduleRepository.findAllByMemberId(userId, StudyGroupMemberStatus.APPROVED)).thenReturn(List.of(memberStudySchedule));

        // when
        List<MemberScheduleCalendarResponseDto> result = memberStudyScheduleService.findAllByMemberId(userId);

        // then
        assertThat(result.size()).isEqualTo(1);
        MemberScheduleCalendarResponseDto dto = result.get(0);
        assertThat(dto.getStudyScheduleId()).isEqualTo(studyScheduleId);
        assertThat(dto.getTitle()).isEqualTo("스터디 일정 제목");
        assertThat(dto.getStartTime()).isEqualTo(LocalDateTime.of(2025, 5, 30, 14, 0));
        assertThat(dto.getEndTime()).isEqualTo(LocalDateTime.of(2025, 5, 30, 16, 0));
    }

    @Test
    @DisplayName("참석 선택 + 중요 일정 체크/메모 추가 성공")
    void updateMemberStudySchedule_Success () {
        // given
        Long memberStudyScheduleId = 1L;

        StudySchedule studySchedule = mock(StudySchedule.class);
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(userId);

        MemberStudySchedule memberStudySchedule = MemberStudySchedule.of(member, studySchedule, false, false, null);

        MemberStudyScheduleRequestDto requestDto = MemberStudyScheduleRequestDto.builder()
                .isAvailable(true)
                .isImportant(true)
                .memo("memo")
                .build();

        when(memberStudyScheduleRepository.findById(memberStudyScheduleId))
                .thenReturn(Optional.of(memberStudySchedule));

        // when
        MemberStudyScheduleResponseDto response = memberStudyScheduleService.updateMemberStudySchedule(memberStudyScheduleId, userId, requestDto);

        // then
        assertThat(response.getId()).isEqualTo(memberStudySchedule.getId());
        assertThat(response.getIsAvailable()).isTrue();
        assertThat(response.getIsImportant()).isTrue();
        assertThat(response.getMemo()).isEqualTo("memo");
        }

    @Test
    @DisplayName("업데이트 실패 - 존재하지 않는 스케줄 id")
    void updateMemberStudySchedule_fail_scheduleNotFound () {
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
        assertThatThrownBy(() -> memberStudyScheduleService.updateMemberStudySchedule(invalidMemberStudyScheduleId, userId, requestDto))
                .isInstanceOf(ScheduleException.class)
                .hasMessageContaining(ScheduleErrorCode.SCHEDULE_NOT_FOUND.getMessage());
        }
}

