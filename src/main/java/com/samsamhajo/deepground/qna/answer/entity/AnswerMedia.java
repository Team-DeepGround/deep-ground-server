package com.samsamhajo.deepground.qna.answer.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answer_media")
@NoArgsConstructor
@Getter
public class AnswerMedia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_media_id", nullable = false)
    private Long id;

    @Column(name = "answer_comment_url",nullable = false)
    private String answerCommentUrl;

    @Column(name = "extension",nullable = false)
    private String extension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer answer;

    private AnswerMedia(String answerCommentUrl, String extension, Answer answer) {
        this.answerCommentUrl = answerCommentUrl;
        this.extension = extension;
        this.answer = answer;
    }

    public static AnswerMedia of(String answerCommentUrl, String extension, Answer answer) {
        return new AnswerMedia(answerCommentUrl, extension, answer);
    }
}
