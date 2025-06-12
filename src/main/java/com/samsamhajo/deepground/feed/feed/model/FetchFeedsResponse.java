package com.samsamhajo.deepground.feed.feed.model;

import lombok.Getter;

import java.util.List;

@Getter
public class FetchFeedsResponse {
    private List<FetchFeedResponse> feeds;

    private FetchFeedsResponse(List<FetchFeedResponse> feeds) {
        this.feeds = feeds;
    }

    public static FetchFeedsResponse of(List<FetchFeedResponse> feeds) {
        return new FetchFeedsResponse(feeds);
    }
} 