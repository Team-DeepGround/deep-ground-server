package com.samsamhajo.deepground.qna.comment.controller;


import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.global.utils.GlobalLogger;
import com.samsamhajo.deepground.qna.comment.dto.*;
import com.samsamhajo.deepground.qna.comment.exception.CommentSuccessCode;
import com.samsamhajo.deepground.qna.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PutMapping({"/{commentId}"})
    public ResponseEntity<SuccessResponse> updateComment(
            @Valid @RequestBody UpdateCommentRequestDto updateCommentRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            ) {
        UpdateCommentResponseDto updateCommentResponseDto = commentService.updateComment(updateCommentRequestDto,
                customUserDetails.getMember().getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(CommentSuccessCode.COMMENT_UPDATED, updateCommentResponseDto));
    }

    @DeleteMapping({"/{commentId}"})
    public ResponseEntity<SuccessResponse> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        commentService.deleteComment(commentId, customUserDetails.getMember().getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(CommentSuccessCode.COMMENT_DELETED));
    }


    @PostMapping
    public ResponseEntity<SuccessResponse> createComment(
            @Valid @RequestBody CreateCommentRequest createCommentRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        CommentCreateResponse commentCreateResponse = commentService.createComment(createCommentRequest,
                customUserDetails.getMember().getId());
        GlobalLogger.info("댓글 생성 = {}",  commentCreateResponse.getCommentId(), "회원ID = {}",  customUserDetails.getMember().getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.of(CommentSuccessCode.COMMENT_CREATED, commentCreateResponse));
    }

    @GetMapping("/comments")
    public ResponseEntity<SuccessResponse>  getComments(
            @RequestParam Long answerId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

       List<CommentDetail> commentDetail = commentService.getComments(answerId, customUserDetails.getMember().getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(CommentSuccessCode.COMMENT_SUCCESS_SEARCH,commentDetail));
    }
}

