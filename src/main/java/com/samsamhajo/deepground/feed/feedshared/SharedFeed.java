package com.samsamhajo.deepground.feed.feedshared;

import com.samsamhajo.deepground.feed.Feed;
import com.samsamhajo.deepground.global.BaseEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor
public class SharedFeed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
