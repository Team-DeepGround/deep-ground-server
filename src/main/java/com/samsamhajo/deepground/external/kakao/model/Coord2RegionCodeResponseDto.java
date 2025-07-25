package com.samsamhajo.deepground.external.kakao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Coord2RegionCodeResponseDto {
    private Meta meta;
    private List<Document> documents;

    @Getter
    @Setter
    public static class Meta {
        @JsonProperty("total_count")
        private int totalCount;
    }

    @Getter
    @Setter
    public static class Document {
        @JsonProperty("region_type")
        private String regionType;
        @JsonProperty("address_name")
        private String addressName;
        @JsonProperty("region_1depth_name")
        private String region1depthName;
        @JsonProperty("region_2depth_name")
        private String region2depthName;
        @JsonProperty("region_3depth_name")
        private String region3depthName;
        @JsonProperty("region_4depth_name")
        private String region4depthName;
        private String code;
        private double x;
        private double y;
    }
} 