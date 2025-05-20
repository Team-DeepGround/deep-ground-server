package com.samsamhajo.deepground.qna.question.controller;

import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.qna.question.Dto.QuestionRequestDto;
import com.samsamhajo.deepground.qna.question.exception.QuestionSuccessCode;
import com.samsamhajo.deepground.qna.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> createQuestion(
            @Valid @ModelAttribute QuestionRequestDto questionRequestDto,
            @RequestParam Long memberId) {

        Long questionId = questionService.createQuestion(questionRequestDto, memberId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.of(QuestionSuccessCode.QUESTION_CREATED, questionId));

    }

    @DeleteMapping("/{questionId}/delete")
    public ResponseEntity<SuccessResponse> deleteQuestion(
            @PathVariable Long questionId
    ,       @RequestParam Long memberId) {

        questionService.deleteQuesiton(questionId, memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(QuestionSuccessCode.QUESTION_DELETED, questionId));

    }

}
