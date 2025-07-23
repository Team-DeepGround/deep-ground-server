package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.address.dto.AddressDto;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupAddress;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupTechTag;
import com.samsamhajo.deepground.studyGroup.entity.TechTag;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyGroupCreateResponse {

  private Long id;
  private String title;
  private String explanation;
  private Boolean isOffline;
  private Set<TechTagDto> techTags;
  private Set<AddressDto> addresses;

  public static StudyGroupCreateResponse from(StudyGroup group) {
    return StudyGroupCreateResponse.builder()
            .id(group.getId())
            .title(group.getTitle())
            .explanation(group.getExplanation())
            .isOffline(group.getIsOffline())
            .addresses(group.getStudyGroupAddresses().stream()
                    .map(StudyGroupAddress::getAddress)
                    .map(AddressDto::from)
                    .collect(Collectors.toSet()))
            .techTags(
                    group.getStudyGroupTechTags().stream()
                            .map(StudyGroupTechTag::getTechStack)
                            .map(TechTagDto::from)
                            .collect(Collectors.toSet())
            )
            .build();
  }
}
