package com.samsamhajo.deepground.qna.question.Dto;

import com.samsamhajo.deepground.qna.question.entity.QuestionStatus;
import lombok.Getter;

import java.util.UUID;

@Getter
public class QuestionUpdateStatusResponseDto {

    private Long questionId;
    private QuestionStatus status;
    private UUID publicId;


   public QuestionUpdateStatusResponseDto(Long questionId, QuestionStatus status, UUID publicId) {
       this.questionId = questionId;
       this.status = status;
       this.publicId = publicId;
   }
   public static QuestionUpdateStatusResponseDto of(Long questionId, QuestionStatus status, UUID publicId) {
       return new QuestionUpdateStatusResponseDto(questionId, status, publicId);
   }
}