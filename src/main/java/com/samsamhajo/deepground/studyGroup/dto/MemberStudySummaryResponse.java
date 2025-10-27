package com.samsamhajo.deepground.studyGroup.dto;

import java.time.LocalDate;
import java.util.List;

public record MemberStudySummaryResponse(
        Long id,
        String title,
        String explanation,
        Boolean isOffline,
        String studyLocation,
        LocalDate studyStartDate,
        LocalDate studyEndDate,
        LocalDate recruitStartDate,
        LocalDate recruitEndDate,
        Integer groupMemberCount,
        Integer currentMemberCount,
        String groupStatus,
        List<TagDto> tags
) {
}
