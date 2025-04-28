package com.samsamhajo.deepground.feed;


import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class FeedComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 4096)
    public String content;

    @ManyToOne
    @JoinColumn(name="feed_id")
    public Feed feed;

//   TODO: Member Entity 등록 시 추가
//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    public Member member;

}
