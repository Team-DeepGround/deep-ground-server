package com.samsamhajo.deepground.calendar.controller;

import com.samsamhajo.deepground.calendar.dto.MemberStudyScheduleRequestDto;
import com.samsamhajo.deepground.calendar.dto.MemberStudyScheduleResponseDto;
import com.samsamhajo.deepground.calendar.exception.ScheduleSuccessCode;
import com.samsamhajo.deepground.calendar.service.MemberStudyScheduleService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calendar/my-schedules")
@RequiredArgsConstructor
public class MemberStudyScheduleController {

    private final MemberStudyScheduleService memberStudyScheduleService;

    @PatchMapping("/{memberStudyScheduleId}")
    public ResponseEntity<SuccessResponse<MemberStudyScheduleResponseDto>> update(
            @PathVariable Long memberStudyScheduleId,
            @RequestBody MemberStudyScheduleRequestDto requestDto
    ) {
        MemberStudyScheduleResponseDto responseDto = memberStudyScheduleService.update(memberStudyScheduleId, requestDto);
        return ResponseEntity.status(ScheduleSuccessCode.SCHEDULE_UPDATED.getStatus())
                .body(SuccessResponse.of(ScheduleSuccessCode.SCHEDULE_UPDATED, responseDto));
    }
}
