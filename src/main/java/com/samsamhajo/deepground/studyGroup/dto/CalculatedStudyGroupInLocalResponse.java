package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.address.dto.AddressDto;
import lombok.Getter;

import java.util.List;


@Getter
public class CalculatedStudyGroupInLocalResponse {
    List<Long> studyGroupIds;
    Long count;
    AddressDto address;

    private CalculatedStudyGroupInLocalResponse(List<Long> studyGroupIds, Long count, AddressDto address) {
        this.studyGroupIds = studyGroupIds;
        this.count = count;
        this.address = address;
    }

    public static CalculatedStudyGroupInLocalResponse of(List<Long> studyGroupIds, Long count, AddressDto address) {
        return new CalculatedStudyGroupInLocalResponse(studyGroupIds, count, address);
    }
}
