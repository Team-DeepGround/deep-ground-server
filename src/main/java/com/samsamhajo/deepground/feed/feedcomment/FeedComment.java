package com.samsamhajo.deepground.feed.feedcomment;


import com.samsamhajo.deepground.feed.Feed;
import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "feed_comments")
@Getter
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

    //   TODO: Member Entity 등록 시 추가
    //    @ManyToOne(fetch = FetchType.LAZY)
    //    @JoinColumn(name = "member_id")
    //    public Member member;

    protected FeedComment() {}

    private FeedComment(String content, Feed feed) {
        this.content = content;
        this.feed = feed;
        // TODO: this.member = member;
    }

    public static FeedComment of(String content, Feed feed) {
        return new FeedComment(content, feed);
    }
}
