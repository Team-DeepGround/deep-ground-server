package com.samsamhajo.deepground.feed.feedshared.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SharedFeedRequest {
    @NotNull(message = "원본 피드 ID는 필수입니다.")
    private Long originFeedId;

    private String content;
} 