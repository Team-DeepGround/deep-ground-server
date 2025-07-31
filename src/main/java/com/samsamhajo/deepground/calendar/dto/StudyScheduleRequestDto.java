package com.samsamhajo.deepground.calendar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class StudyScheduleRequestDto {

    @NotBlank(message = "스터디 스케줄의 제목은 필수입니다.")
    private String title;

    @NotNull(message = "시작 시간은 필수입니다.")
    private LocalDateTime startTime;

    @NotNull(message = "종료 시간은 필수입니다.")
    private LocalDateTime endTime;

    @NotNull(message = "스터디 설명은 필수입니다.")
    private String description;

    private String location;

    @DecimalMin(value = "-90.0", message = "위도는 -90도 이상이어야 합니다.")
    @DecimalMax(value = "90.0", message = "위도는 90도 이하여야 합니다.")
    private Double latitude;

    @DecimalMin(value = "-180.0", message = "경도는 -180도 이상이어야 합니다.")
    @DecimalMax(value = "180.0", message = "경도는 180도 이하여야 합니다.")
    private Double longitude;

    private PlaceRequestDto place;
}
