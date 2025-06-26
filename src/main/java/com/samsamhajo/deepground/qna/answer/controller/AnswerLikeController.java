    package com.samsamhajo.deepground.qna.answer.controller;

    import com.samsamhajo.deepground.auth.security.CustomUserDetails;
    import com.samsamhajo.deepground.global.success.SuccessResponse;
    import com.samsamhajo.deepground.qna.answer.exception.AnswerSuccessCode;
    import com.samsamhajo.deepground.qna.answer.service.AnswerLikeService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/answers/like")
    @RequiredArgsConstructor
    public class AnswerLikeController {

        private final AnswerLikeService answerLikeService;

        @PostMapping("/{answerId}")
        public ResponseEntity<SuccessResponse> AnswerLike(
                @PathVariable Long answerId,
                @AuthenticationPrincipal CustomUserDetails customUserDetails
        ) {
            answerLikeService.answerLike(answerId, customUserDetails.getMember().getId());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(SuccessResponse.of(AnswerSuccessCode.ANSWER_LIKED));
        }

        @DeleteMapping("/{answerId}")
        public ResponseEntity<SuccessResponse> deleteAnswerLike(
                @PathVariable Long answerId,
                @AuthenticationPrincipal CustomUserDetails customUserDetails
        ) {
            answerLikeService.answerUnLike(answerId, customUserDetails.getMember().getId());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(SuccessResponse.of(AnswerSuccessCode.ANSWER_UNLIKED));
        }
    }
