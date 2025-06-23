package com.samsamhajo.deepground.calendar.dto;

import com.samsamhajo.deepground.calendar.entity.MemberStudySchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberScheduleCalendarResponseDto {

    private Long memberStudyScheduleId;
    private Long studyScheduleId;
    private Long studyGroupId;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isAvailable;
    private Boolean isImportant;
    private String memo;


    public static MemberScheduleCalendarResponseDto from(MemberStudySchedule schedule) {
        var ss = schedule.getStudySchedule();
        var sg = ss.getStudyGroup();
        return MemberScheduleCalendarResponseDto.builder()
                .memberStudyScheduleId(schedule.getId())
                .studyScheduleId(ss.getId())
                .studyGroupId(sg.getId())
                .title(ss.getTitle())
                .startTime(ss.getStartTime())
                .endTime(ss.getEndTime())
                .isAvailable(schedule.getIsAvailable())
                .isImportant(schedule.getIsImportant())
                .memo(schedule.getMemo())
                .build();
    }
}
