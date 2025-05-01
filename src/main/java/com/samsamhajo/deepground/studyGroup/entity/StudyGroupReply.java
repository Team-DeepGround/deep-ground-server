package com.samsamhajo.deepground.studyGroup.entity;

import com.samsamhajo.deepground.global.BaseEntity;
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

    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "content", nullable = false)
    private String content;
}
