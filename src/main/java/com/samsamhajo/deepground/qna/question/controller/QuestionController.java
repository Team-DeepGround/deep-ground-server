package com.samsamhajo.deepground.qna.question.controller;

import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.qna.question.Dto.QuestionRequestDto;
import com.samsamhajo.deepground.qna.question.Dto.QuestionResponseDto;
import com.samsamhajo.deepground.qna.question.Dto.QuestionUpdateDto;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.exception.QuestionSuccessCode;
import com.samsamhajo.deepground.qna.question.repository.QuestionRepository;
import com.samsamhajo.deepground.qna.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionRepository questionRepository;

    @PostMapping
    public ResponseEntity<SuccessResponse> createQuestion(
            @Valid @ModelAttribute QuestionRequestDto questionRequestDto,
            @RequestParam Long memberId) {

        Question question = questionService.createQuestion(questionRequestDto, memberId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.of(QuestionSuccessCode.QUESTION_CREATED, question));

    }

    @DeleteMapping("/{questionId}/delete")
    public ResponseEntity<SuccessResponse> deleteQuestion(
            @PathVariable Long questionId
            ,       @RequestParam Long memberId) {

        questionService.deleteQuestion(questionId, memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(QuestionSuccessCode.QUESTION_DELETED, questionId));

    }


    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> updateQuestion(
            @Valid @ModelAttribute QuestionUpdateDto questionUpdateDto,
            @RequestParam Long memberId) {

        QuestionResponseDto questionResponseDto = questionService.updateQuestion(questionUpdateDto, memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(QuestionSuccessCode.QUESTION_UPDATED, questionResponseDto));
    }


}