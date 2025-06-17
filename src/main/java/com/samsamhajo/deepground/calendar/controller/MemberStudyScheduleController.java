package com.samsamhajo.deepground.calendar.controller;


import com.samsamhajo.deepground.auth.security.CustomUserDetails;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendar/my-schedules")
@RequiredArgsConstructor
public class MemberStudyScheduleController {

    private final MemberStudyScheduleService memberStudyScheduleService;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<MemberScheduleCalendarResponseDto>>> findMemberStudySchedules(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getMember().getId();

        List<MemberScheduleCalendarResponseDto> memberStudySchedules = memberStudyScheduleService.findAllByMemberId(userId);

        return ResponseEntity.status(ScheduleSuccessCode.SCHEDULE_FOUND.getStatus())
                .body(SuccessResponse.of(ScheduleSuccessCode.SCHEDULE_FOUND, memberStudySchedules));
    }

    @GetMapping("/{memberStudyScheduleId}")
    public ResponseEntity<SuccessResponse<MemberScheduleDetailResponseDto>> getScheduleByMemberScheduleId(
            @PathVariable Long memberStudyScheduleId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getMember().getId();

        MemberScheduleDetailResponseDto responseDto = memberStudyScheduleService.getScheduleByMemberScheduleId(memberStudyScheduleId, userId);

        return ResponseEntity.status(ScheduleSuccessCode.SCHEDULE_FOUND.getStatus())
                .body(SuccessResponse.of(ScheduleSuccessCode.SCHEDULE_FOUND, responseDto));
    }

    @PatchMapping("/{memberStudyScheduleId}")
    public ResponseEntity<SuccessResponse<MemberStudyScheduleResponseDto>> updateByMemberScheduleId(
            @PathVariable Long memberStudyScheduleId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody MemberStudyScheduleRequestDto requestDto
    ) {
        Long userId = userDetails.getMember().getId();

        MemberStudyScheduleResponseDto responseDto = memberStudyScheduleService.updateMemberStudySchedule(memberStudyScheduleId, userId, requestDto);
        return ResponseEntity.status(ScheduleSuccessCode.SCHEDULE_UPDATED.getStatus())
                .body(SuccessResponse.of(ScheduleSuccessCode.SCHEDULE_UPDATED, responseDto));
    }
}
