package com.samsamhajo.deepground.calendar.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberScheduleCalendarResponseDto {

    private Long memberStudyScheduleId;
    private Long studyScheduleId;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private String location;
    private Boolean isAvailable;
    private Boolean isImportant;
    private String memo;
}
