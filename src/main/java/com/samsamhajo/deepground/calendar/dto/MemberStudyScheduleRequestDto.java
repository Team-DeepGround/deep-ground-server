package com.samsamhajo.deepground.calendar.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class MemberStudyScheduleRequestDto {

    private Boolean isAvailable;

    @NotNull
    private Boolean isImportant;

    @Size(max = 100, message = "메모는 100자 이내로 작성해주세요.")
    private String memo;
}
