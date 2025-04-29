package com.samsamhajo.deepground.feed.feedcomment;


import com.samsamhajo.deepground.feed.feedcomment.FeedComment;
import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
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

}
