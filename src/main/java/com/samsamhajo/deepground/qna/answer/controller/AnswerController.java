package com.samsamhajo.deepground.qna.answer.controller;

import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.qna.answer.dto.AnswerRequestDto;
import com.samsamhajo.deepground.qna.answer.dto.AnswerResponseDto;
import com.samsamhajo.deepground.qna.answer.service.AnswerService;
import com.samsamhajo.deepground.qna.answer.exception.AnswerSuccessCode;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<SuccessResponse> createAnswer  (
            @Valid @RequestBody AnswerRequestDto answerRequestDto,
            @RequestParam Long memberId,
            @RequestParam Long questionId
    )
    {
        AnswerResponseDto answerResponseDto = answerService.createAnswer(answerRequestDto, memberId, questionId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.of(AnswerSuccessCode.ANSWER_CREATED, answerResponseDto));

    }
}