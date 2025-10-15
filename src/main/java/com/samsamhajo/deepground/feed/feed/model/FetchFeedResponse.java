package com.samsamhajo.deepground.feed.feed.model;

import com.samsamhajo.deepground.feed.feedshared.model.FetchSharedFeedResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class FetchFeedResponse {
    private long memberId;
    private long feedId;
    private String memberName;
    private String content;
    private int likeCount;
    private String profileImageUrl;

    private boolean isLiked;

    private int commentCount;
    private int shareCount;
    private int profileImageId;
    private LocalDate createdAt;
    private List<Long> mediaIds;

    private boolean isShared;
    private FetchSharedFeedResponse sharedFeed;
} 