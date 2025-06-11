package com.samsamhajo.deepground.feed.feed.service;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.exception.FeedErrorCode;
import com.samsamhajo.deepground.feed.feed.exception.FeedException;
import com.samsamhajo.deepground.feed.feed.model.*;
import com.samsamhajo.deepground.feed.feed.repository.FeedRepository;
import com.samsamhajo.deepground.feed.feedcomment.service.FeedCommentService;
import com.samsamhajo.deepground.feed.feedshared.model.FetchSharedFeedResponse;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.exception.MemberErrorCode;
import com.samsamhajo.deepground.member.exception.MemberException;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.feed.feedshared.service.SharedFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

    private final FeedRepository feedRepository;
    private final FeedMediaService feedMediaService;
    private final MemberRepository memberRepository;
    private final FeedCommentService feedCommentService;
    private final FeedLikeService feedLikeService;
    private final SharedFeedService sharedFeedService;

    @Transactional
    public Feed createFeed(FeedCreateRequest request, Long memberId) {
        if (!StringUtils.hasText(request.getContent())) {
            throw new FeedException(FeedErrorCode.INVALID_FEED_CONTENT);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.INVALID_MEMBER_ID));

        Feed feed = Feed.of(request.getContent(), member);

        feedRepository.save(feed);

        saveFeedMedia(request, feed);

        return feed;
    }

    @Transactional
    public Feed updateFeed(Long feedId, FeedUpdateRequest request, Long memberId) {
        if (!StringUtils.hasText(request.getContent())) {
            throw new FeedException(FeedErrorCode.INVALID_FEED_CONTENT);
        }

        Feed feed = feedRepository.getById(feedId);

        // 피드 내용 업데이트
        feed.updateContent(request.getContent());

        // 미디어 업데이트
        feedMediaService.updateFeedMedia(feed, request);

        return feed;
    }


    public FetchFeedsResponse getFeeds(Pageable pageable, Long memberId) {
        return FetchFeedsResponse.of(
                feedRepository.findAll(pageable).stream()
                        .map(feed -> {
                            FetchSharedFeedResponse sharedFeedResponse =
                                    sharedFeedService.getSharedFeedResponse(feed.getId());

                            return FetchFeedResponse.builder()
                                    .feedId(feed.getId())
                                    .content(feed.getContent())
                                    .createdAt(feed.getCreatedAt().toLocalDate())
                                    .memberId(feed.getMember().getId())
                                    .memberName(feed.getMember().getNickname())
                                    .mediaIds(feedMediaService.findAllMediaIdsByFeedId(feed.getId()))
                                    .shareCount(sharedFeedService.countSharedFeedByOriginFeedId(feed.getId()))
                                    .commentCount(feedCommentService.countFeedCommentsByFeedId(feed.getId()))
                                    .likeCount(feedLikeService.countFeedLikeByFeedId(feed.getId()))
                                    .isLiked(feedLikeService.isLiked(feed.getId(), memberId))
                                    .isShared(sharedFeedResponse != null)
                                    .sharedFeed(sharedFeedResponse)
                                    .build();
                        }
                        ).toList());
    }

    private void saveFeedMedia(FeedCreateRequest request, Feed feed) {
        feedMediaService.createFeedMedia(feed, request.getImages());
    }

    @Transactional
    public void deleteFeed(Long feedId) {

        deleteRelatedEntities(feedId);

        feedRepository.deleteById(feedId);
    }

    private void deleteRelatedEntities(Long feedId) {
        feedCommentService.deleteFeedCommentByFeed(feedId);
        feedLikeService.deleteAllByFeedId(feedId);
        feedMediaService.deleteAllByFeedId(feedId);
    }
}