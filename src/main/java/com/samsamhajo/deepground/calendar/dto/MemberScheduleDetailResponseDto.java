package com.samsamhajo.deepground.calendar.dto;

import com.samsamhajo.deepground.calendar.entity.MemberStudySchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberScheduleDetailResponseDto {

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

    public static MemberScheduleDetailResponseDto from(MemberStudySchedule schedule) {
        var ss = schedule.getStudySchedule();
        return MemberScheduleDetailResponseDto.builder()
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
    }
}

