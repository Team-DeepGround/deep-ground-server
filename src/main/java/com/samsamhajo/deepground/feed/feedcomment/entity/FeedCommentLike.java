package com.samsamhajo.deepground.feed.feedcomment.entity;


import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feed_comment_likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedCommentLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_comment_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private FeedComment feedComment;

//   TODO: Member Entity 등록 시 추가
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    public Member member;


    private FeedCommentLike(FeedComment feedComment) {
        this.feedComment = feedComment;
        // TODO: this.member = member;
    }

    public static FeedCommentLike of(FeedComment feedComment){
        return new FeedCommentLike(feedComment);
    }
}
