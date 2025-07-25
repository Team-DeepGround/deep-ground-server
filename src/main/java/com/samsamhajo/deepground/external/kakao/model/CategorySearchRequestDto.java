package com.samsamhajo.deepground.external.kakao.model;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class CategorySearchRequestDto {
    private final String categoryGroupCode;
    private final String x;
    private final String y;
    private final Integer radius;
    private final Integer page;
    private final Integer size;
    private final String sort;

    public Map<String, String> toParam() {
        Map<String, String> params = new HashMap<>();
        params.put("category_group_code", categoryGroupCode);
        if (x != null) params.put("x", x);
        if (y != null) params.put("y", y);
        if (radius != null) params.put("radius", radius.toString());
        if (page != null) params.put("page", page.toString());
        if (size != null) params.put("size", size.toString());
        if (sort != null) params.put("sort", sort);
        return params;
    }
} 