package com.samsamhajo.deepground.feed.feedcomment.entity;


import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feed_comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_comment_id")
    private Long id;

    @Column(length = 4096, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private FeedComment(String content, Feed feed, Member member) {
        this.content = content;
        this.feed = feed;
        this.member = member;
    }

    public static FeedComment of(String content, Feed feed, Member member) {
        return new FeedComment(content, feed, member);
    }
}
