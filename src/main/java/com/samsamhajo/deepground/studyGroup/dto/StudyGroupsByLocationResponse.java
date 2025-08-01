package com.samsamhajo.deepground.studyGroup.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class StudyGroupsByLocationResponse {

    private final List<StudyGroupResponse> studyGroups;

    private StudyGroupsByLocationResponse(List<StudyGroupResponse> studyGroups) {
        this.studyGroups = studyGroups;
    }

    public static StudyGroupsByLocationResponse of(List<StudyGroupResponse> studyGroups) {
        return new StudyGroupsByLocationResponse(studyGroups);
    }
}
