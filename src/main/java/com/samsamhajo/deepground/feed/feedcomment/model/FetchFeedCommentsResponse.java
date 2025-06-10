package com.samsamhajo.deepground.feed.feedcomment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class FetchFeedCommentsResponse {
    private List<FetchFeedCommentResponse> feedComments;

    private FetchFeedCommentsResponse(List<FetchFeedCommentResponse> feedComments) {
        this.feedComments = feedComments;
    }

    public static FetchFeedCommentsResponse of(List<FetchFeedCommentResponse> feedComments) {
        return new FetchFeedCommentsResponse(feedComments);
    }
}