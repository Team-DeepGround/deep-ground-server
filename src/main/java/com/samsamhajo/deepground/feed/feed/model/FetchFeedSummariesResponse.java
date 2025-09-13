package com.samsamhajo.deepground.feed.feed.model;

import lombok.Getter;

import java.util.List;

@Getter
public class FetchFeedSummariesResponse {
    private List<FetchFeedSummaryResponse> feedSummaries;
    private long total;
    private int page;
    private int pageSize;
    private int totalPages;

    private FetchFeedSummariesResponse(List<FetchFeedSummaryResponse> feedSummaries) {
        this.feedSummaries = feedSummaries;
    }

    private FetchFeedSummariesResponse(List<FetchFeedSummaryResponse> feedSummaries, long total, int page, int pageSize, int totalPages) {
        this.feedSummaries = feedSummaries;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
    }

    public static FetchFeedSummariesResponse of(List<FetchFeedSummaryResponse> feedSummaries) {
        return new FetchFeedSummariesResponse(feedSummaries);
    }

    public static FetchFeedSummariesResponse of(List<FetchFeedSummaryResponse> feedSummaries, long total, int page, int pageSize, int totalPages) {
        return new FetchFeedSummariesResponse(
                feedSummaries,
                total,
                page,
                pageSize,
                totalPages
        );
    }
} 