package com.samsamhajo.deepground.feed.feedreply.model;

import lombok.Getter;

import java.util.List;

@Getter
public class FetchFeedRepliesResponse {
    private List<FetchFeedReplyResponse> feedReplies;

    private FetchFeedRepliesResponse(List<FetchFeedReplyResponse> feedReplies) {
        this.feedReplies = feedReplies;
    }

    public static FetchFeedRepliesResponse of(List<FetchFeedReplyResponse> feedReplies) {
        return new FetchFeedRepliesResponse(feedReplies);
    }
} 