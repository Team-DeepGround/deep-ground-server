package com.samsamhajo.deepground.calendar.controller;

import com.samsamhajo.deepground.calendar.dto.StudyScheduleRequestDto;
import com.samsamhajo.deepground.calendar.dto.StudyScheduleResponseDto;
import com.samsamhajo.deepground.calendar.service.StudyScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/study-group/{studyGroupId}")
@RequiredArgsConstructor
public class StudyScheduleController {

    private final StudyScheduleService studyScheduleService;

    @PostMapping("/schedules")
    public ResponseEntity<StudyScheduleResponseDto> createSchedule(
            @PathVariable Long studyGroupId,
            @Valid @RequestBody StudyScheduleRequestDto requestDto
    ) {
        StudyScheduleResponseDto responseDto = studyScheduleService.createStudySchedule(studyGroupId, requestDto);
        return ResponseEntity.ok(responseDto);
    }



}
