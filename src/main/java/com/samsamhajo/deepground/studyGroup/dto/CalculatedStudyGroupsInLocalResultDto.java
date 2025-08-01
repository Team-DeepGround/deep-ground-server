package com.samsamhajo.deepground.studyGroup.dto;

import lombok.Getter;

import java.util.List;


@Getter
public class CalculatedStudyGroupsInLocalResultDto {
    List<Long> studyGroupIds;
    Long count;
    Long addressId;
}
