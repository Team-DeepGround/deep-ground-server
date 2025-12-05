package com.samsamhajo.deepground.feed.feed.repository;

import com.samsamhajo.deepground.feed.feed.model.v2.FetchFeedResponseV2;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface FeedRepositoryCustom {

    Slice<FetchFeedResponseV2> findFeeds(Pageable pageable, Long memberId);
}
