package com.samsamhajo.deepground.calendar.service;


import com.samsamhajo.deepground.calendar.dto.MemberScheduleCalendarResponseDto;
import com.samsamhajo.deepground.calendar.dto.MemberStudyScheduleRequestDto;
import com.samsamhajo.deepground.calendar.dto.MemberStudyScheduleResponseDto;

import com.samsamhajo.deepground.calendar.entity.MemberStudySchedule;
import com.samsamhajo.deepground.calendar.exception.ScheduleErrorCode;
import com.samsamhajo.deepground.calendar.exception.ScheduleException;
import com.samsamhajo.deepground.calendar.repository.MemberStudyScheduleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberStudyScheduleService {

    private final MemberStudyScheduleRepository memberStudyScheduleRepository;

    @Transactional(readOnly = true)
    public List<MemberScheduleCalendarResponseDto> findAllByMemberId(Long userId) {

        List<MemberStudySchedule> schedules = memberStudyScheduleRepository.findAllByMemberId(userId);

        return schedules.stream()
                .map(MemberScheduleCalendarResponseDto::from)
                .toList();
    }

    @Transactional
    public MemberStudyScheduleResponseDto updateMemberStudySchedule (Long memberScheduleId, Long userId, MemberStudyScheduleRequestDto requestDto){

        MemberStudySchedule memberStudySchedule = validateScheduleOwner(memberScheduleId, userId);

        if (requestDto.getIsAvailable() != null) {
            memberStudySchedule.updateAvailable(requestDto.getIsAvailable());

            if (Boolean.FALSE.equals(memberStudySchedule.getIsAvailable())) {
                memberStudySchedule.updateImportant(false);
                memberStudySchedule.updateMemo(null);
            }
        }

        if (requestDto.getIsImportant() != null) {
            memberStudySchedule.updateImportant(requestDto.getIsImportant());
        }

        if (requestDto.getMemo() != null) {
            memberStudySchedule.updateMemo(requestDto.getMemo());
        }

        return MemberStudyScheduleResponseDto.from(memberStudySchedule);
    }

    private MemberStudySchedule validateScheduleOwner(Long memberScheduleId, Long userId) {
        MemberStudySchedule schedule = memberStudyScheduleRepository.findById(memberScheduleId)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        if (!schedule.getMember().getId().equals(userId)) {
            throw new ScheduleException(ScheduleErrorCode.UNAUTHORIZED_USER);
        }
        return schedule;
    }
}

