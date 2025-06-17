package com.samsamhajo.deepground.qna.answer.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.qna.answer.dto.AnswerCreateRequestDto;
import com.samsamhajo.deepground.qna.answer.dto.AnswerCreateResponseDto;
import com.samsamhajo.deepground.qna.answer.dto.AnswerUpdateRequestDto;
import com.samsamhajo.deepground.qna.answer.dto.AnswerUpdateResponseDto;
import com.samsamhajo.deepground.qna.answer.service.AnswerService;
import com.samsamhajo.deepground.qna.answer.exception.AnswerSuccessCode;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> createAnswer  (
            @Valid @ModelAttribute AnswerCreateRequestDto answerCreateRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    )
    {
        AnswerCreateResponseDto answerCreateResponseDto = answerService.createAnswer(answerCreateRequestDto
        , customUserDetails.getMember().getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.of(AnswerSuccessCode.ANSWER_CREATED, answerCreateResponseDto));

    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<SuccessResponse> deleteAnswer(
            @PathVariable Long answerId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){

                answerService.deleteAnswer(answerId, customUserDetails.getMember().getId());

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(SuccessResponse.of(AnswerSuccessCode.ANSWER_DELETED));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<AnswerUpdateResponseDto>> updateAnswer(
            @Valid @ModelAttribute AnswerUpdateRequestDto answerUpdateRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        AnswerUpdateResponseDto answerUpdateResponseDto = answerService.updateAnswer(answerUpdateRequestDto,
                customUserDetails.getMember().getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(AnswerSuccessCode.ANSWER_UPDATED, answerUpdateResponseDto));

    }

}


