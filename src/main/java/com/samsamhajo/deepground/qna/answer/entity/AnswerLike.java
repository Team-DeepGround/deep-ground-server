package com.samsamhajo.deepground.qna.answer.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "answer_likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_like_id", nullable = false)
    private Long id;

    //TODO : Member, Question

}
