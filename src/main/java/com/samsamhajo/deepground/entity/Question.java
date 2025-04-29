package com.samsamhajo.deepground.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor //JPA 사용을 위해 필요 (기본생성자 생성)
@AllArgsConstructor // 필드를 다 받는 생성자
@Getter
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "question_id", nullable = false, length = 200)
    private String question_id;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionStatus questionStatus = QuestionStatus.NOT_ACCEPTED;

    @Column(nullable = false) //DB에 null값이 들어가면 +1 연산할 때 문제가 발생하거나 예외 발생할 수 있어 nullable = false;
    private int answer_count = 0;

    @Column(nullable = false)
    private int view_count = 0;


    //TODO : Answer, QuestionLike, QuestionMedia, QuestionTag 와 연관간계 매핑


    //TODO : Member Entity 등록 시 추가
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColum(name = "memeber_id")




}
