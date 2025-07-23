package com.samsamhajo.deepground.communityPlace.dto.response;

import lombok.Getter;
import java.util.List;

@Getter
public class ReviewResponseDto {
    private Long id;
    private double scope;
    private String content;
    private String location;
    private double latitude;
    private double longitude;
    private List<String> mediaUrls;

    public ReviewResponseDto(Long id, double scope, String content,
                             String location, double latitude, double longitude,
                             List<String> mediaUrls) {
        this.id = id;
        this.scope = scope;
        this.content = content;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mediaUrls = mediaUrls;
    }

    public static ReviewResponseDto of(Long id, double scope, String content,
                                       String location, double latitude, double longitude,
                                       List<String> mediaUrls) {
        return new ReviewResponseDto(id, scope, content, location, latitude, longitude, mediaUrls);
    }
}
