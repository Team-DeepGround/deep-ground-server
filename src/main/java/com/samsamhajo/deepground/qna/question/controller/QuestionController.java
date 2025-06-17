package com.samsamhajo.deepground.qna.question.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.qna.question.Dto.*;
import com.samsamhajo.deepground.qna.question.exception.QuestionSuccessCode;
import com.samsamhajo.deepground.qna.question.repository.QuestionRepository;
import com.samsamhajo.deepground.qna.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> createQuestion(
            @Valid @ModelAttribute QuestionCreateRequestDto questionRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {


        QuestionCreateResponseDto questionCreateResponseDto = questionService.createQuestion(questionRequestDto, customUserDetails.getMember().getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.of(QuestionSuccessCode.QUESTION_CREATED, questionCreateResponseDto));

    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> updateQuestion(
            @Valid @ModelAttribute QuestionUpdateRequestDto questionUpdateRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        QuestionUpdateResponseDto questionResponseDto = questionService.updateQuestion(questionUpdateRequestDto, customUserDetails.getMember().getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(QuestionSuccessCode.QUESTION_UPDATED, questionResponseDto));
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<SuccessResponse> deleteQuestion(
            @PathVariable Long questionId
            ,@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        questionService.deleteQuestion(questionId, customUserDetails.getMember().getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(QuestionSuccessCode.QUESTION_DELETED, questionId));

    }

    @PatchMapping("/{questionId}/status")
    public ResponseEntity<SuccessResponse> updateStatusQuestion(
            @Valid @RequestBody QuestionUpdateStatusRequestDto questionUpdateStatusRequestDto,
            @PathVariable Long questionId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        System.out.println("questionId = " + questionId);
        System.out.println("memberId = " + customUserDetails.getMember().getId());

        QuestionUpdateStatusResponseDto questionUpdateStatusResponseDto = questionService.updateQuestionStatus(questionUpdateStatusRequestDto,
                customUserDetails.getMember().getId(), questionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(QuestionSuccessCode.QUESTION_STATUS_UPDATED, questionUpdateStatusResponseDto));
    }


}