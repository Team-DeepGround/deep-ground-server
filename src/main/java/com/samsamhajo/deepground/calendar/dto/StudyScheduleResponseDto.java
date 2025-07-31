package com.samsamhajo.deepground.calendar.dto;

import com.samsamhajo.deepground.calendar.entity.StudySchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudyScheduleResponseDto {

    private Long id;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private String location;
    private Double latitude;
    private Double longitude;
    private SpecificAddressResponseDto place;

    public static StudyScheduleResponseDto from(StudySchedule schedule) {
        return StudyScheduleResponseDto.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .description(schedule.getDescription())
                .location(schedule.getLocation())
                .latitude(schedule.getLatitude())
                .longitude(schedule.getLongitude())
                .place(schedule.getSpecificAddress() != null
                        ? SpecificAddressResponseDto.from(schedule.getSpecificAddress())
                        : null)
                .build();
    }

}
