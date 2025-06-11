package com.samsamhajo.deepground.feed.feedshared.repository;

import com.samsamhajo.deepground.feed.feedshared.entity.SharedFeed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedFeedRepository extends JpaRepository<SharedFeed, Long> {
} 