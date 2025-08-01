package com.samsamhajo.deepground.calendar.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceRequestDto {
    private String name;
    private String address;
    private String phone;
    private String placeUrl;
    private Double latitude;
    private Double longitude;
}
