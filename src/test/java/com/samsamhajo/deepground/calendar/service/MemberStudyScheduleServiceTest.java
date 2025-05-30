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
import com.samsamhajo.deepground.member.repository.MemberRepository;
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
    private MemberRepository memberRepository;

    @Mock
    private MemberStudyScheduleRepository memberStudyScheduleRepository;

    @InjectMocks
    private MemberStudyScheduleService memberStudyScheduleService;

    @Test
    @DisplayName("멤버 ID로 멤버 스터디 일정 조회 성공")
    void findMemberSchedulesByMemberId_Success() {
        // given
        Long memberId = 1L;
        Long studyScheduleId = 10L;
        Member member = mock(Member.class);
        StudySchedule studySchedule = mock(StudySchedule.class);

        // studySchedule mock 설정
        when(studySchedule.getId()).thenReturn(studyScheduleId);
        when(studySchedule.getTitle()).thenReturn("스터디 일정 제목");
        when(studySchedule.getStartTime()).thenReturn(LocalDateTime.of(2025, 5, 30, 14, 0));
        when(studySchedule.getEndTime()).thenReturn(LocalDateTime.of(2025, 5, 30, 16, 0));
        when(studySchedule.getDescription()).thenReturn("스터디 일정 설명");
        when(studySchedule.getLocation()).thenReturn("온라인");

        MemberStudySchedule memberStudySchedule = MemberStudySchedule.of(studySchedule, true, true, "memo");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberStudyScheduleRepository.findAllByMemberId(memberId)).thenReturn(List.of(memberStudySchedule));

        // when
        List<MemberScheduleCalendarResponseDto> result = memberStudyScheduleService.findAllByMemberId(memberId);

        // then
        assertThat(result.size()).isEqualTo(1);
        MemberScheduleCalendarResponseDto dto = result.get(0);
        assertThat(dto.getMemberStudyScheduleId()).isEqualTo(memberStudySchedule.getId());
        assertThat(dto.getStudyScheduleId()).isEqualTo(studyScheduleId);
        assertThat(dto.getTitle()).isEqualTo("스터디 일정 제목");
        assertThat(dto.getStartTime()).isEqualTo(LocalDateTime.of(2025, 5, 30, 14, 0));
        assertThat(dto.getEndTime()).isEqualTo(LocalDateTime.of(2025, 5, 30, 16, 0));
        assertThat(dto.getDescription()).isEqualTo("스터디 일정 설명");
        assertThat(dto.getLocation()).isEqualTo("온라인");
        assertThat(dto.getIsAvailable()).isTrue();
        assertThat(dto.getIsImportant()).isTrue();
        assertThat(dto.getMemo()).isEqualTo("memo");
    }

    @Test
    @DisplayName("멤버 스터디 일정 조회 실패 - 존재하지 않는 멤버")
    void findMemberSchedulesByMemberId_Fail_MemberNotFound() {
        // given
        Long memberId = 1L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberStudyScheduleService.findAllByMemberId(memberId))
                .isInstanceOf(ScheduleException.class)
                .hasMessageContaining(ScheduleErrorCode.MEMBER_NOT_FOUND.getMessage());

        verify(memberRepository).findById(memberId);
        verify(memberStudyScheduleRepository, never()).findAllByMemberId(anyLong());
    }

    @Test
    @DisplayName("참석 선택 + 중요 일정 체크/메모 추가 성공")
    void update_MemberStudySchedule_Success () {
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
        assertThatThrownBy(() -> memberStudyScheduleService.update(invalidMemberStudyScheduleId, requestDto))
                .isInstanceOf(ScheduleException.class)
                .hasMessageContaining(ScheduleErrorCode.SCHEDULE_NOT_FOUND.getMessage());
        }
}

