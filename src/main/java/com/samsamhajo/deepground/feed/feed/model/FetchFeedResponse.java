package com.samsamhajo.deepground.feed.feed.model;

import com.samsamhajo.deepground.feed.feedshared.model.FetchSharedFeedResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class FetchFeedResponse {
    private UUID publicId;
    private UUID profilePublicId;
    private Long feedId;
    private String memberName;
    private String content;
    private int likeCount;

    private boolean isLiked;

    private int commentCount;
    private int shareCount;
    private String profileImageUrl;
    private LocalDate createdAt;
    private List<String> mediaUrls;

    private boolean isShared;
    private FetchSharedFeedResponse sharedFeed;
} 
