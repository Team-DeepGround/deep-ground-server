package com.samsamhajo.deepground.studyGroup.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.member.entity.Member;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "study_group_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id", nullable = false)
    private StudyGroup studyGroup;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "comment")
    private List<StudyGroupReply> replies = new ArrayList<>();

    private StudyGroupComment(StudyGroup studyGroup, Member member, String content) {
        this.studyGroup = studyGroup;
        this.member = member;
        this.content = content;
    }

    public static StudyGroupComment of(StudyGroup studyGroup, Member member, String content) {
        return new StudyGroupComment(studyGroup, member, content);
    }
}
