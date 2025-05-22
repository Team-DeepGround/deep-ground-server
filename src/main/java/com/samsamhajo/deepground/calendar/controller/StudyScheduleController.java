package com.samsamhajo.deepground.calendar.controller;

import com.samsamhajo.deepground.calendar.dto.StudyScheduleRequestDto;
import com.samsamhajo.deepground.calendar.dto.StudyScheduleResponseDto;
import com.samsamhajo.deepground.calendar.exception.ScheduleSuccessCode;
import com.samsamhajo.deepground.calendar.service.StudyScheduleService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/study-group/{studyGroupId}")
@RequiredArgsConstructor
public class StudyScheduleController {

    private final StudyScheduleService studyScheduleService;

    @PostMapping("/schedules")
    public ResponseEntity<SuccessResponse<StudyScheduleResponseDto>> createSchedule(
            @PathVariable Long studyGroupId,
            @Valid @RequestBody StudyScheduleRequestDto requestDto
    ) {
        StudyScheduleResponseDto responseDto = studyScheduleService.createStudySchedule(studyGroupId, requestDto);
        return ResponseEntity.status(ScheduleSuccessCode.SCHEDULE_CREATED.getStatus())
                .body(SuccessResponse.of(ScheduleSuccessCode.SCHEDULE_CREATED, responseDto));
    }

    @GetMapping("/schedules")
    public ResponseEntity<SuccessResponse<List<StudyScheduleResponseDto>>> getStudySchedulesByGroup(
            @PathVariable Long studyGroupId
    ) {
        List<StudyScheduleResponseDto> schedules = studyScheduleService.findSchedulesByStudyGroupId(studyGroupId);

        return ResponseEntity.status(ScheduleSuccessCode.SCHEDULE_FOUND.getStatus())
                .body(SuccessResponse.of(ScheduleSuccessCode.SCHEDULE_FOUND, schedules));
    }

}
