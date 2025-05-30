package com.samsamhajo.deepground.calendar.service;


import com.samsamhajo.deepground.calendar.dto.MemberScheduleCalendarResponseDto;
import com.samsamhajo.deepground.calendar.dto.MemberStudyScheduleRequestDto;
import com.samsamhajo.deepground.calendar.dto.MemberStudyScheduleResponseDto;

import com.samsamhajo.deepground.calendar.entity.MemberStudySchedule;
import com.samsamhajo.deepground.calendar.exception.ScheduleErrorCode;
import com.samsamhajo.deepground.calendar.exception.ScheduleException;
import com.samsamhajo.deepground.calendar.repository.MemberStudyScheduleRepository;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberStudyScheduleService {

    private final MemberRepository memberRepository;
    private final MemberStudyScheduleRepository memberStudyScheduleRepository;

    @Transactional(readOnly = true)
    public List<MemberScheduleCalendarResponseDto> findAllByMemberId(Long memberId) {

        validateMember(memberId);

        List<MemberStudySchedule> schedules = memberStudyScheduleRepository.findAllByMemberId(memberId);
        return schedules.stream()
                .map(MemberScheduleCalendarResponseDto::from)
                .toList();
    }

    @Transactional
    public MemberStudyScheduleResponseDto update (Long scheduleId, MemberStudyScheduleRequestDto requestDto){

        MemberStudySchedule schedule = memberStudyScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        if (requestDto.getIsAvailable() != null) {
            schedule.updateAvailable(requestDto.getIsAvailable());

            if (Boolean.FALSE.equals(schedule.getIsAvailable())) {
                schedule.updateImportant(false);
                schedule.updateMemo(null);
            } else {
                // 참석 중일 때만 개별 필드 업데이트
                if (requestDto.getIsImportant() != null) {
                    schedule.updateImportant(requestDto.getIsImportant());
                }
                if (requestDto.getMemo() != null) {
                    schedule.updateMemo(requestDto.getMemo());
                }
            }
        }

        return MemberStudyScheduleResponseDto.builder()
                .id(schedule.getId())
                .isAvailable(schedule.getIsAvailable())
                .isImportant(schedule.getIsImportant())
                .memo(schedule.getMemo())
                .build();
    }

    private void validateMember(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.MEMBER_NOT_FOUND));
    }
}

