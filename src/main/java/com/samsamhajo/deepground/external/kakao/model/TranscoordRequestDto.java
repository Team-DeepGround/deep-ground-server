package com.samsamhajo.deepground.external.kakao.model;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class TranscoordRequestDto {
    private final String x;
    private final String y;
    private final String inputCoord;
    private final String outputCoord;

    public Map<String, String> toParam() {
        Map<String, String> params = new HashMap<>();
        params.put("x", x);
        params.put("y", y);
        params.put("input_coord", inputCoord);
        params.put("output_coord", outputCoord);
        return params;
    }
} 