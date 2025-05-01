package com.samsamhajo.deepground.feed.feedreply.entity;


import com.samsamhajo.deepground.feed.feedcomment.entity.FeedComment;
import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feed_replies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_reply_id")
    private Long id;

    @Column(length = 4096, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private FeedComment feedComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    private FeedReply(String content, FeedComment feedComment, Member member) {
        this.content = content;
        this.feedComment = feedComment;
        this.member = member;
    }

    public static FeedReply of(String content, FeedComment feedComment, Member member) {
        return new FeedReply(content, feedComment, member);
    }
}
