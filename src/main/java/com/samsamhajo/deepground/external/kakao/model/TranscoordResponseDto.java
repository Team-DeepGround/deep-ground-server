package com.samsamhajo.deepground.external.kakao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class TranscoordResponseDto {
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
        private double x;
        private double y;
    }
} 