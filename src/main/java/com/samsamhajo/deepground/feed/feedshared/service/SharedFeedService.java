package com.samsamhajo.deepground.feed.feedshared.service;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feedshared.model.FetchSharedFeedResponse;
import com.samsamhajo.deepground.feed.feed.repository.FeedRepository;
import com.samsamhajo.deepground.feed.feed.service.FeedMediaService;
import com.samsamhajo.deepground.feed.feedshared.dto.SharedFeedRequest;
import com.samsamhajo.deepground.feed.feedshared.entity.SharedFeed;
import com.samsamhajo.deepground.feed.feedshared.repository.SharedFeedRepository;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.exception.MemberErrorCode;
import com.samsamhajo.deepground.member.exception.MemberException;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SharedFeedService {

    private final SharedFeedRepository sharedFeedRepository;
    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;
    private final FeedMediaService feedMediaService;

    @Transactional
    public SharedFeed createSharedFeed(SharedFeedRequest request, Long memberId) {
        Feed originFeed = feedRepository.getById(request.getOriginFeedId());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.INVALID_MEMBER_ID));

        Feed feed = Feed.of(request.getContent(), member);
        feedRepository.save(feed);

        SharedFeed sharedFeed = SharedFeed.of(feed, originFeed, member);
        sharedFeedRepository.save(sharedFeed);
        
        return sharedFeed;
    }

    public FetchSharedFeedResponse getSharedFeedResponse(Long feedId){
        SharedFeed sharedFeed = findOrNullByFeedId(feedId);

        List<Long> sharedFeedMediaIds =
                feedMediaService.findAllMediaIdsByFeedId(sharedFeed.getOriginFeed().getId());

        return FetchSharedFeedResponse.toDto(sharedFeed, sharedFeedMediaIds);
    }

    public int countSharedFeedByOriginFeedId(Long originFeedId) {
        return sharedFeedRepository.countAllByOriginFeedId(originFeedId);
    }

    public SharedFeed findByFeedId(Long feedId) {
        return sharedFeedRepository.getByFeedId(feedId);
    }

    public SharedFeed findOrNullByFeedId(Long feedId) {
        return sharedFeedRepository.getOrNullByFeedId(feedId);
    }
} 