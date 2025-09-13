package com.samsamhajo.deepground.feed.feedshared.service;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.repository.FeedRepository;
import com.samsamhajo.deepground.feed.feed.service.FeedMediaService;
import com.samsamhajo.deepground.feed.feedshared.dto.SharedFeedRequest;
import com.samsamhajo.deepground.feed.feedshared.entity.SharedFeed;
import com.samsamhajo.deepground.feed.feedshared.model.FetchSharedFeedResponse;
import com.samsamhajo.deepground.feed.feedshared.repository.SharedFeedRepository;
import com.samsamhajo.deepground.member.entity.Member;
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
    private final FeedMediaService feedMediaService;

    @Transactional
    public SharedFeed createSharedFeed(SharedFeedRequest request, Member member) {
        Feed originFeed = feedRepository.getById(request.getOriginFeedId());

        Feed feed = Feed.of(request.getContent(), member);
        feedRepository.save(feed);

        SharedFeed sharedFeed = SharedFeed.of(feed, originFeed, member);
        sharedFeedRepository.save(sharedFeed);
        
        return sharedFeed;
    }

    public FetchSharedFeedResponse getSharedFeedResponse(Long feedId){
        SharedFeed sharedFeed = findOrNullByFeedId(feedId);

        if(sharedFeed == null){
            return null;
        }

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