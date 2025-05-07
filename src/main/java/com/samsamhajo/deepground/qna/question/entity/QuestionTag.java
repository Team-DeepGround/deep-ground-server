package com.samsamhajo.deepground.qna.question.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.techStack.entity.TechStack;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "question_tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_tag_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teck_stack_id")
    private TechStack techStack;

    public QuestionTag(Question question, TechStack techStack) {
        this.question = question;
        this.techStack = techStack;
    }
}
