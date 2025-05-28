package com.samsamhajo.deepground.calendar.service;

import com.samsamhajo.deepground.calendar.dto.MemberScheduleCalendarResponseDto;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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

        MemberStudySchedule memberStudySchedule = MemberStudySchedule.of(studySchedule, member, true, true, "memo");

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
}
