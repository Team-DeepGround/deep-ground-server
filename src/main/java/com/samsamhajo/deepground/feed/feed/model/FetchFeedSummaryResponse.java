package com.samsamhajo.deepground.feed.feed.model;

import com.samsamhajo.deepground.feed.feedshared.model.FetchSharedFeedResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class FetchFeedSummaryResponse {
    private long feedId;
    private String memberName;
    private String content;
    private LocalDate createdAt;
} 