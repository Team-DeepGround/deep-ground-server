package com.samsamhajo.deepground.external.kakao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AddressSearchResponseDto {
    private Meta meta;
    private List<Document> documents;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Meta {
        @JsonProperty("total_count")
        private int totalCount;
        @JsonProperty("pageable_count")
        private int pageableCount;
        @JsonProperty("is_end")
        private boolean isEnd;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Document {
        @JsonProperty("address_name")
        private String addressName;
        private String y;
        private String x;
        @JsonProperty("address_type")
        private String addressType;
        private Address address;
        @JsonProperty("road_address")
        private RoadAddress roadAddress;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Address {
        @JsonProperty("address_name")
        private String addressName;
        @JsonProperty("region_1depth_name")
        private String region1depthName;
        @JsonProperty("region_2depth_name")
        private String region2depthName;
        @JsonProperty("region_3depth_name")
        private String region3depthName;
        @JsonProperty("region_3depth_h_name")
        private String region3depthHName;
        @JsonProperty("h_code")
        private String hCode;
        @JsonProperty("b_code")
        private String bCode;
        @JsonProperty("mountain_yn")
        private String mountainYn;
        @JsonProperty("main_address_no")
        private String mainAddressNo;
        @JsonProperty("sub_address_no")
        private String subAddressNo;
        private String x;
        private String y;
    }

    @Getter
    @Setter
    @AllArgsConstructor
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
        private String y;
        private String x;
    }
} 