package com.samsamhajo.deepground.feed.feedreply.entity;


import com.samsamhajo.deepground.feed.feedcomment.entity.FeedComment;
import com.samsamhajo.deepground.global.BaseEntity;
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
    @JoinColumn(name="feed_id")
    private FeedComment feedComment;

    //   TODO: Member Entity 등록 시 추가
    //    @ManyToOne(fetch = FetchType.LAZY)
    //    @JoinColumn(name = "member_id")
    //    public Member member;


    private FeedReply(String content, FeedComment feedComment) {
        this.content = content;
        this.feedComment = feedComment;
        // TODO: this.member = member;
    }

    public static FeedReply of(String content, FeedComment feedComment) {
        return new FeedReply(content, feedComment);
    }
}
