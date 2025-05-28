package com.samsamhajo.deepground.calendar.service;

import com.samsamhajo.deepground.calendar.dto.MemberScheduleCalendarResponseDto;
import com.samsamhajo.deepground.calendar.entity.MemberStudySchedule;
import com.samsamhajo.deepground.calendar.exception.ScheduleErrorCode;
import com.samsamhajo.deepground.calendar.exception.ScheduleException;
import com.samsamhajo.deepground.calendar.repository.MemberStudyScheduleRepository;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .map(schedule -> {
                    var ss = schedule.getStudySchedule(); // StudySchedule 객체 가져오기
                    return MemberScheduleCalendarResponseDto.builder()
                            .memberStudyScheduleId(schedule.getId())
                            .studyScheduleId(ss.getId())
                            .title(ss.getTitle())
                            .startTime(ss.getStartTime())
                            .endTime(ss.getEndTime())
                            .description(ss.getDescription())
                            .location(ss.getLocation())
                            .isAvailable(schedule.getIsAvailable())
                            .isImportant(schedule.getIsImportant())
                            .memo(schedule.getMemo())
                            .build();
                })
                .toList();
    }

    private void validateMember(Long memberId) {
        if (memberRepository.findById(memberId).isEmpty()) {
            throw new ScheduleException(ScheduleErrorCode.MEMBER_NOT_FOUND);
        }
    }
}
