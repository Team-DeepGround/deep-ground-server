package com.samsamhajo.deepground.calendar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

}
