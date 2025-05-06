package com.samsamhajo.deepground.studyGroup.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "study_group_replies")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_group_reply_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private StudyGroupComment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String content;

    private StudyGroupReply(StudyGroupComment comment, Member member, String content) {
        this.comment = comment;
        this.member = member;
        this.content = content;
    }

    public static StudyGroupReply of(StudyGroupComment comment, Member member, String content) {
        return new StudyGroupReply(comment, member, content);
    }
}
