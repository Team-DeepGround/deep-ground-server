package com.samsamhajo.deepground.external.kakao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Coord2AddressResponseDto {
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
        @JsonProperty("road_address")
        private RoadAddress roadAddress;
        private Address address;
    }

    @Getter
    @Setter
    public static class Address {
        @JsonProperty("address_name")
        private String addressName;
        @JsonProperty("region_1depth_name")
        private String region1depthName;
        @JsonProperty("region_2depth_name")
        private String region2depthName;
        @JsonProperty("region_3depth_name")
        private String region3depthName;
        @JsonProperty("mountain_yn")
        private String mountainYn;
        @JsonProperty("main_address_no")
        private String mainAddressNo;
        @JsonProperty("sub_address_no")
        private String subAddressNo;
    }

    @Getter
    @Setter
    public static class RoadAddress {
        @JsonProperty("address_name")
        private String addressName;
        @JsonProperty("region_1depth_name")
        private String region1depthName;
        @JsonProperty("region_2depth_name")
        private String region2depthName;
        @JsonProperty("region_3depth_name")
        private String region3depthName;
        @JsonProperty("road_name")
        private String roadName;
        @JsonProperty("underground_yn")
        private String undergroundYn;
        @JsonProperty("main_building_no")
        private String mainBuildingNo;
        @JsonProperty("sub_building_no")
        private String subBuildingNo;
        @JsonProperty("building_name")
        private String buildingName;
        @JsonProperty("zone_no")
        private String zoneNo;
    }
} 