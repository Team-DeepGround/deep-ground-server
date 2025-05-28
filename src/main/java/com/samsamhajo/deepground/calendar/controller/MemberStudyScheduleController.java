package com.samsamhajo.deepground.calendar.controller;

import com.samsamhajo.deepground.calendar.dto.MemberScheduleCalendarResponseDto;
import com.samsamhajo.deepground.calendar.exception.ScheduleSuccessCode;
import com.samsamhajo.deepground.calendar.service.MemberStudyScheduleService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calendar/my-schedules")
@RequiredArgsConstructor
public class MemberStudyScheduleController {

    private final MemberStudyScheduleService memberStudyScheduleService;

    @GetMapping("/{memberId}")
    public ResponseEntity<SuccessResponse<List<MemberScheduleCalendarResponseDto>>> getMemberStudySchedulesByMemberId(
            @PathVariable Long memberId
    ) {
        List<MemberScheduleCalendarResponseDto> memberStudySchedules = memberStudyScheduleService.findAllByMemberId(memberId);

        return ResponseEntity.status(ScheduleSuccessCode.SCHEDULE_FOUND.getStatus())
                .body(SuccessResponse.of(ScheduleSuccessCode.SCHEDULE_FOUND, memberStudySchedules));
    }
}
