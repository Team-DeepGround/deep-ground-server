package com.samsamhajo.deepground.external.kakao.model;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class Coord2RegionCodeRequestDto {
    private final String x;
    private final String y;
    private final String inputCoord;
    private final String outputCoord;

    public Map<String, String> toParam() {
        Map<String, String> params = new HashMap<>();
        params.put("x", x);
        params.put("y", y);
        if (inputCoord != null) params.put("input_coord", inputCoord);
        if (outputCoord != null) params.put("output_coord", outputCoord);
        return params;
    }
} 