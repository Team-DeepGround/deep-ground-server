package com.samsamhajo.deepground.communityPlace.dto;

import lombok.Getter;

import java.awt.*;

@Getter
public class CommunityPlaceReviewDto {

    private String name;
    private Long number;

    private CommunityPlaceReviewDto(String name, Long number) {
        this.name = name;
        this.number = number;
    }

}
