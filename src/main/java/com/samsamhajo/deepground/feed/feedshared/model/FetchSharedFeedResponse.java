package com.samsamhajo.deepground.feed.feedshared.model;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feedshared.entity.SharedFeed;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class FetchSharedFeedResponse {
    private Long feedId;
    private Long memberId;
    private Long profileId;
    private String memberName;
    private String content;
    private String profileImageUrl;
    private LocalDate createdAt;
    private List<Long> mediaIds;


    public static FetchSharedFeedResponse toDto(SharedFeed sharedFeed, List<Long> mediaIds) {
        Feed originFeed = sharedFeed.getOriginFeed();

        return FetchSharedFeedResponse.builder()
                .feedId(originFeed.getId())
                .memberId(originFeed.getMember().getId())
                .profileId(originFeed.getMember().getMemberProfile().getProfileId())
                .memberName(originFeed.getMember().getNickname())
                .content(originFeed.getContent())
                .createdAt(originFeed.getCreatedAt().toLocalDate())
                .mediaIds(mediaIds)
                .build();
    }
}
