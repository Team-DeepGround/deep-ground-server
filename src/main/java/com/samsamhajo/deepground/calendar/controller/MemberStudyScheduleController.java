package com.samsamhajo.deepground.calendar.controller;


import com.samsamhajo.deepground.calendar.dto.MemberScheduleCalendarResponseDto;
import com.samsamhajo.deepground.calendar.dto.MemberScheduleDetailResponseDto;
import com.samsamhajo.deepground.calendar.dto.MemberStudyScheduleRequestDto;
import com.samsamhajo.deepground.calendar.dto.MemberStudyScheduleResponseDto;
import com.samsamhajo.deepground.calendar.exception.ScheduleSuccessCode;
import com.samsamhajo.deepground.calendar.service.MemberStudyScheduleService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendar/my-schedules")
@RequiredArgsConstructor
public class MemberStudyScheduleController {

    private final MemberStudyScheduleService memberStudyScheduleService;

    @GetMapping("/member/{memberId}")
    public ResponseEntity<SuccessResponse<List<MemberScheduleCalendarResponseDto>>> findMemberStudySchedulesByMemberId(
            @PathVariable Long memberId
    ) {
        List<MemberScheduleCalendarResponseDto> memberStudySchedules = memberStudyScheduleService.findAllByMemberId(memberId);

        return ResponseEntity.status(ScheduleSuccessCode.SCHEDULE_FOUND.getStatus())
                .body(SuccessResponse.of(ScheduleSuccessCode.SCHEDULE_FOUND, memberStudySchedules));
    }

    @GetMapping("/{memberStudyScheduleId}")
    public ResponseEntity<SuccessResponse<MemberScheduleDetailResponseDto>> getScheduleByMemberScheduleId(
            @PathVariable Long memberStudyScheduleId
    ) {
        MemberScheduleDetailResponseDto responseDto = memberStudyScheduleService.getScheduleByMemberScheduleId(memberStudyScheduleId);

        return ResponseEntity.status(ScheduleSuccessCode.SCHEDULE_FOUND.getStatus())
                .body(SuccessResponse.of(ScheduleSuccessCode.SCHEDULE_FOUND, responseDto));
    }

    @PatchMapping("/{memberStudyScheduleId}")
    public ResponseEntity<SuccessResponse<MemberStudyScheduleResponseDto>> updateByMemberScheduleId(
            @PathVariable Long memberStudyScheduleId,
            @Valid @RequestBody MemberStudyScheduleRequestDto requestDto
    ) {
        MemberStudyScheduleResponseDto responseDto = memberStudyScheduleService.updateMemberStudySchedule(memberStudyScheduleId, requestDto);
        return ResponseEntity.status(ScheduleSuccessCode.SCHEDULE_UPDATED.getStatus())
                .body(SuccessResponse.of(ScheduleSuccessCode.SCHEDULE_UPDATED, responseDto));
    }
}
