package com.samsamhajo.deepground.external.kakao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CategorySearchResponseDto {
    private Meta meta;
    private List<Document> documents;

    @Getter
    @Setter
    public static class Meta {
        @JsonProperty("same_name")
        private Object sameName; // null or object, 실제 사용시 타입 정의 필요
        @JsonProperty("pageable_count")
        private int pageableCount;
        @JsonProperty("total_count")
        private int totalCount;
        @JsonProperty("is_end")
        private boolean isEnd;
    }

    @Getter
    @Setter
    public static class Document {
        @JsonProperty("place_name")
        private String placeName;
        private String distance;
        @JsonProperty("place_url")
        private String placeUrl;
        @JsonProperty("category_name")
        private String categoryName;
        @JsonProperty("address_name")
        private String addressName;
        @JsonProperty("road_address_name")
        private String roadAddressName;
        private String id;
        private String phone;
        @JsonProperty("category_group_code")
        private String categoryGroupCode;
        @JsonProperty("category_group_name")
        private String categoryGroupName;
        private String x;
        private String y;
    }
} 