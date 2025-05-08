package com.samsamhajo.deepground.qna.question.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table (name = "question_media")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionMedia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_media_id", nullable = false)
    private Long id;

    @Column(name="question_content_url", nullable = false)
    private String questionContentUrl;

    @Column(name = "extension", nullable = false)
    private String extension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    private QuestionMedia(String QuestionContentUrl, String extension, Question question) {
        this.questionContentUrl = QuestionContentUrl;
        this.extension = extension;
        this.question = question;
    }
    public static QuestionMedia of(String QuestionContentUrl, String extension, Question question) {
        return new QuestionMedia(QuestionContentUrl, extension, question);
    }

}
