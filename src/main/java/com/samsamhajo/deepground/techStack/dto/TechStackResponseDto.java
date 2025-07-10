package com.samsamhajo.deepground.techStack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TechStackResponseDto {
    private Long id;
    private String name;
    private String category;
} 