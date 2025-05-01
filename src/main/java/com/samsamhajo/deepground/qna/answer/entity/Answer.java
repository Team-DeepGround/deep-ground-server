package com.samsamhajo.deepground.qna.answer.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "answers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id",nullable = false)
    private Long id;

    @Column(name = "answer_content", nullable = false, columnDefinition = "TEXT")
    private String answerContent;

    @Column(name = "answer_like_count",nullable = false)
    private int answerLikeCount = 0;

    @Column(name = "comment_count",nullable = false)
    private int commentCount = 0;
}
