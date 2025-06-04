package com.samsamhajo.deepground.calendar.service;


import com.samsamhajo.deepground.calendar.dto.MemberScheduleCalendarResponseDto;
import com.samsamhajo.deepground.calendar.dto.MemberScheduleDetailResponseDto;
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
    public MemberScheduleDetailResponseDto getScheduleByMemberScheduleId(Long memberScheduleId) {

        MemberStudySchedule memberStudySchedule = memberStudyScheduleRepository.findById(memberScheduleId)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        return MemberScheduleDetailResponseDto.from(memberStudySchedule);
    }

    @Transactional
    public MemberStudyScheduleResponseDto updateMemberStudySchedule (Long memberScheduleId, MemberStudyScheduleRequestDto requestDto){

        MemberStudySchedule memberStudySchedule = memberStudyScheduleRepository.findById(memberScheduleId)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        if (requestDto.getIsAvailable() != null) {
            memberStudySchedule.updateAvailable(requestDto.getIsAvailable());

            if (Boolean.FALSE.equals(memberStudySchedule.getIsAvailable())) {
                memberStudySchedule.updateImportant(false);
                memberStudySchedule.updateMemo(null);
            } else {
                // 참석 중일 때만 개별 필드 업데이트
                if (requestDto.getIsImportant() != null) {
                    memberStudySchedule.updateImportant(requestDto.getIsImportant());
                }
                if (requestDto.getMemo() != null) {
                    memberStudySchedule.updateMemo(requestDto.getMemo());
                }
            }
        }

        return MemberStudyScheduleResponseDto.from(memberStudySchedule);
    }

    private void validateMember(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.MEMBER_NOT_FOUND));
    }
}

