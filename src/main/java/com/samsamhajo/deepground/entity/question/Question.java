package com.samsamhajo.deepground.entity.question;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "questions")
@NoArgsConstructor //JPA 사용을 위해 필요 (기본생성자 생성)
@Getter
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "question_status",nullable = false)
    private boolean questionStatus = false;

    @Column(name ="answer_count",nullable = false)//DB에 null값이 들어가면 +1 연산할 때 문제가 발생하거나 예외 발생할 수 있어 nullable = false;
    private int answerCount = 0;

    @Column(name = "view_count",nullable = false)
    private int viewCount = 0;



}
