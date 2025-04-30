package com.samsamhajo.deepground.answer;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "answer_likes")
@NoArgsConstructor
public class AnswerLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_like_id", nullable = false)
    private Long id;

    //TODO : Member, Question

}
