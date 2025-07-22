package com.samsamhajo.deepground.communityPlace.dto.response;

import com.samsamhajo.deepground.communityPlace.dto.request.AddressDto;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewResponseDto {
    private long id;
    private double scope;
    private String content;
    private List<AddressDto> address;
}
