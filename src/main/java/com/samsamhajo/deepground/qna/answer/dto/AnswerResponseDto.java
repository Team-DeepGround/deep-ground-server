package com.samsamhajo.deepground.qna.answer.dto;

import com.samsamhajo.deepground.qna.answer.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnswerResponseDto {

    private Long id;
    private String answerContent;
    private Long questionId;
    private Long memberId;

}