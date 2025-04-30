package com.samsamhajo.deepground.entity.answer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "answer_content", nullable = false)
    private String answer_content;

    @Column(nullable = false)
    private int answer_like_count = 0;

    @Column(nullable = false)
    private int comment_count = 0;
}
