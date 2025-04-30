package com.samsamhajo.deepground.entity.answer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "answer_id",nullable = false)
    private Long id;

    @Column(name = "answer_content", nullable = false, columnDefinition = "TEXT")
    private String answer_content;

    @Column(name = "answer_like_count",nullable = false)
    private int answerLikeCount = 0;

    @Column(name = "comment_count",nullable = false)
    private int commentCount = 0;
}
