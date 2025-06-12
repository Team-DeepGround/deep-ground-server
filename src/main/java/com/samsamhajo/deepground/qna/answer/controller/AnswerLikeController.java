package com.samsamhajo.deepground.qna.answer.controller;

import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.qna.answer.exception.AnswerSuccessCode;
import com.samsamhajo.deepground.qna.answer.repository.AnswerLikeRepository;
import com.samsamhajo.deepground.qna.answer.service.AnswerLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answer/like")
@RequiredArgsConstructor
public class AnswerLikeController {

    private final AnswerLikeRepository answerLikeRepository;
    private final AnswerLikeService answerLikeService;

    private final Long memberId = 1L;

    @PostMapping("/{answerId}")
    public ResponseEntity<SuccessResponse> AnswerLike(
            @PathVariable Long answerId
    ) {
        answerLikeService.answerLike(answerId, memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(AnswerSuccessCode.ANSWER_LIKED));
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<SuccessResponse> deleteAnswerLike(
            @PathVariable Long answerId
    ) {
        answerLikeService.answerUnLike(answerId, memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(AnswerSuccessCode.ANSWER_UNLIKED));
    }
}
