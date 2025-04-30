package com.samsamhajo.deepground.entity.question;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@Table (name = "question_Media")
public class QuestionMedia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_media_id", nullable = false)
    private Long id;

    @Column(name="question_content_url")
    private String questionContentUrl;

    @Column(name = "extension")
    private String extension;
}
