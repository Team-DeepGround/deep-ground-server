package com.samsamhajo.deepground.feed.feedcomment.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class FetchFeedCommentResponse {

    private long memberId;
    private long feedCommentId;
    private String memberName;
    private String content;
    private int replyCount;
    private int likeCount;
    private boolean isLiked;
    private int profileImageId;
    private LocalDate createdAt;

    private List<Long> mediaIds;
}