package com.samsamhajo.deepground.external.kakao.model;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class AddressSearchRequestDto {
    private final String query;
    private final Integer page;
    private final Integer size;
    private final String analyzeType;

    public Map<String, String> toParam() {
        Map<String, String> params = new HashMap<>();
        params.put("query", query);
        if (page != null) params.put("page", page.toString());
        if (size != null) params.put("size", size.toString());
        if (analyzeType != null) params.put("analyze_type", analyzeType);
        return params;
    }
} 