package com.samsamhajo.deepground.feed.feed.entity;


import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feeds")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    @Column(length = 4096, nullable = false)
    private String content;

//   TODO: Member Entity 등록 시 추가
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    public Member member;

    private Feed(String content) {
        this.content = content;
        // TODO: this.member = member;
    }

    public static Feed of(String content) {
        return new Feed(content);
    }
}
