package com.samsamhajo.deepground.feed.feedshared.entity;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shared_feeds")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SharedFeed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shared_feed_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_feed_id")
    private Feed originFeed; // 원본 피드

    // TODO: Member Entity 구현 시 작성
    //    @ManyToOne(fetch = FetchType.LAZY)
    //    @JoinColumn(name = "member_id")
    //    private Member member;

    private SharedFeed(Feed feed, Feed originFeed) {
        this.feed = feed;
        this.originFeed = originFeed;
        // TODO: this.member = member;
    }

    public static SharedFeed of(Feed feed, Feed originFeed){
        return new SharedFeed(feed,originFeed);
    }
}
