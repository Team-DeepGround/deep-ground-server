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
        // GeometryFactory : 위도, 경도를 계산하기 위한 공간 데이터 객체
            if (latitude == null || longitude == null) {
                    throw new IllegalArgumentException("위도와 경도는 필수값입니다.");
                }
            double lat = latitude;
            double lon = longitude;

        //Coordinate : Point 클래스를 이용하여 좌표를 표현하는데 사용
        return geometryFactory.createPoint(new Coordinate(lon, lat));
    }
}

