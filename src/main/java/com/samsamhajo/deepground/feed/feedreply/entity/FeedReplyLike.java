package com.samsamhajo.deepground.feed.feedreply.entity;


import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feed_reply_likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedReplyLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_reply_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_reply_id")
    private FeedReply feedReply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    private FeedReplyLike(FeedReply feedReply, Member member) {
        this.feedReply = feedReply;
        this.member = member;
    }

    public static FeedReplyLike of(FeedReply feedReply, Member member) {
        return new FeedReplyLike(feedReply, member);
    }
}
