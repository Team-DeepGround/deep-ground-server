package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.techStack.entity.TechStack;
import lombok.Builder;

@Builder
public record TechTagDto(Long id, String name) {

  public static TechTagDto from(TechStack techStack) {
    return TechTagDto.builder()
        .id(techStack.getId())
        .name(techStack.getName())
        .build();
  }
}