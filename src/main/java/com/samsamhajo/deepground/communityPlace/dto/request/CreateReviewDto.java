package com.samsamhajo.deepground.communityPlace.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateReviewDto {
    private double scope;
    private String content;
    private List<AddressDto> address;
}
