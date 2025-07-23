package com.samsamhajo.deepground.communityPlace.dto.request;

import lombok.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Getter
@Setter
@NoArgsConstructor
public class AddressDto {
    private String address;
    private Double latitude;
    private Double longitude;

    public Point toPoint(GeometryFactory geometryFactory) {
        // latitude, longitude가 null이면 0.0으로 처리하거나 예외 처리
        double lat = latitude != null ? latitude : 0.0;
        double lon = longitude != null ? longitude : 0.0;

        return geometryFactory.createPoint(new Coordinate(lon, lat));
    }
}

