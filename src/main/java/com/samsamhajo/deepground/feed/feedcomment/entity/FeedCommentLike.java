package com.samsamhajo.deepground.feed.feedcomment.entity;


import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @JoinColumn(name = "feed_comment_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FeedComment feedComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    private FeedCommentLike(FeedComment feedComment,Member member) {
        this.feedComment = feedComment;
        this.member = member;
    }

    public static FeedCommentLike of(FeedComment feedComment, Member member){
        return new FeedCommentLike(feedComment, member);
    }
}
