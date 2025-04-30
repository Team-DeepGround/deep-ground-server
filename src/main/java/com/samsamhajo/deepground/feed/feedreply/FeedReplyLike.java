package com.samsamhajo.deepground.feed.feedreply;


import com.samsamhajo.deepground.feed.feedcomment.FeedComment;
import com.samsamhajo.deepground.feed.feedcomment.FeedCommentLike;
import com.samsamhajo.deepground.feed.feedreply.FeedReply;
import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feed_reply_likes")
@Getter
public class FeedReplyLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_reply_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private FeedReply feedReply;

//   TODO: Member Entity 등록 시 추가
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    public Member member;

    protected FeedReplyLike() {}

    private FeedReplyLike(FeedReply feedReply) {
        this.feedReply = feedReply;
        // TODO: this.member = member;
    }

    public static FeedReplyLike of(FeedReply feedReply){
        return new FeedReplyLike(feedReply);
    }
}
