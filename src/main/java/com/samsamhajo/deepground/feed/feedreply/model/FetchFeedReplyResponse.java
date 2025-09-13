package com.samsamhajo.deepground.feed.feedreply.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class FetchFeedReplyResponse {
    private long memberId;
    private long feedReplyId;
    private String memberName;
    private String content;
    private int likeCount;
    private int profileImageId;
    private boolean isLiked;
    private LocalDate createdAt;
    private List<Long> mediaIds;
} 