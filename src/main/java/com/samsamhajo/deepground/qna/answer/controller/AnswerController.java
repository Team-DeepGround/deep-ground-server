package com.samsamhajo.deepground.qna.answer.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.qna.answer.dto.*;
import com.samsamhajo.deepground.qna.answer.service.AnswerService;
import com.samsamhajo.deepground.qna.answer.exception.AnswerSuccessCode;
import com.samsamhajo.deepground.qna.question.Dto.QuestionDetailResponseDto;
import com.samsamhajo.deepground.qna.question.exception.QuestionSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> createAnswer  (
            @Valid @ModelAttribute AnswerCreateRequestDto answerCreateRequestDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    )
    {

        answerCreateRequestDto.setImages(images);

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

    @PutMapping(value = "/{answerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> updateAnswer(
            @Valid @ModelAttribute AnswerUpdateRequestDto answerUpdateRequestDto,
            @PathVariable Long answerId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        AnswerUpdateResponseDto answerUpdateResponseDto = answerService.updateAnswer(answerUpdateRequestDto,
                customUserDetails.getMember().getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(AnswerSuccessCode.ANSWER_UPDATED, answerUpdateResponseDto));

    }

    @GetMapping("/{answerId}")
    public ResponseEntity<AnswerEditResponseDto> getAnswer(@PathVariable Long answerId,
                                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        AnswerEditResponseDto response = answerService.getAnswerEditInfo(answerId, customUserDetails.getMember().getId());
        return ResponseEntity.ok(response);
    }




}


