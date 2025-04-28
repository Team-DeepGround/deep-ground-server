package com.samsamhajo.deepground.feed.feedcomment;


import com.samsamhajo.deepground.feed.feedcomment.FeedComment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FeedCommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    public FeedComment feedComment;

//   TODO: Member Entity 등록 시 추가
//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    public Member member;

}
