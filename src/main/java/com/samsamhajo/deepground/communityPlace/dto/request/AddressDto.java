package com.samsamhajo.deepground.communityPlace.dto.request;

import lombok.Getter;
import org.locationtech.jts.geom.Point;

@Getter
public class AddressDto {
    private String location;
    private Point locationPoint;
}
