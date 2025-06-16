package com.samsamhajo.deepground.qna.question.controller;

import com.samsamhajo.deepground.global.success.SuccessCode;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.qna.question.exception.QuestionSuccessCode;
import com.samsamhajo.deepground.qna.question.service.QuestionTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question/tag")
public class QuestionTagController {

    private final QuestionTagService questionTagService;

    @GetMapping("/{questionId}")
    public ResponseEntity<SuccessResponse> getQuestionTag(
           @RequestParam Long questionId //Long techStackId//
    ) {
        questionTagService.TagList(questionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(QuestionSuccessCode.QUESTION_TAG));
    }
}
