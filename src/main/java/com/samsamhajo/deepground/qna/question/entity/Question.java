package com.samsamhajo.deepground.qna.question.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA 사용을 위해 필요 (기본생성자 생성)
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_question_status",nullable = false)
    private boolean isQuestionStatus = false;

    @Column(name ="answer_count",nullable = false)//DB에 null값이 들어가면 +1 연산할 때 문제가 발생하거나 예외 발생할 수 있어 nullable = false;
    private int answerCount = 0;

    @Column(name = "view_count",nullable = false)
    private int viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Question(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public static Question of(String title, String content, Member member) {
        return new Question(title, content, member);
    }

    public void questionActive() {
        this.isQuestionStatus = true;
    }

    public void questionDeactive() {
        this.isQuestionStatus = false;
    }

}
