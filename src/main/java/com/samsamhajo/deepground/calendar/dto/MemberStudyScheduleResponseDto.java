package com.samsamhajo.deepground.calendar.dto;

import com.samsamhajo.deepground.calendar.entity.MemberStudySchedule;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberStudyScheduleResponseDto {

    private Long id;
    private Boolean isAvailable;
    private Boolean isImportant;
    private String memo;

    public static MemberStudyScheduleResponseDto from(MemberStudySchedule schedule) {
        return MemberStudyScheduleResponseDto.builder()
                .id(schedule.getId())
                .isAvailable(schedule.getIsAvailable())
                .isImportant(schedule.getIsImportant())
                .memo(schedule.getMemo())
                .build();
    }
}
