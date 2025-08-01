package com.samsamhajo.deepground.studyGroup.dto;

import lombok.Getter;

import java.util.List;


@Getter
public class CalculatedStudyGroupsInLocalResponse {
    List<CalculatedStudyGroupInLocalResponse> calculatedStudyGroups;

    private CalculatedStudyGroupsInLocalResponse(List<CalculatedStudyGroupInLocalResponse> calculatedStudyGroups) {
        this.calculatedStudyGroups = calculatedStudyGroups;
    }

    public static CalculatedStudyGroupsInLocalResponse of(List<CalculatedStudyGroupInLocalResponse> calculatedStudyGroups) {
        return new CalculatedStudyGroupsInLocalResponse(calculatedStudyGroups);
    }
}
