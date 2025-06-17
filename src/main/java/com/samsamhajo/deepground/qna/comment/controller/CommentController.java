package com.samsamhajo.deepground.qna.comment.controller;


import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessCode;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.qna.comment.dto.UpdateCommentRequestDto;
import com.samsamhajo.deepground.qna.comment.dto.UpdateCommentResponseDto;
import com.samsamhajo.deepground.qna.comment.entity.Comment;
import com.samsamhajo.deepground.qna.comment.dto.CommentCreateRequestDto;
import com.samsamhajo.deepground.qna.comment.dto.CommentCreateResponseDto;
import com.samsamhajo.deepground.qna.comment.exception.CommentSuccessCode;
import com.samsamhajo.deepground.qna.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PutMapping
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
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long answerId
    ) {

        commentService.deleteComment(commentId, customUserDetails.getMember().getId(), answerId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(CommentSuccessCode.COMMENT_DELETED));
    }


    @PostMapping
    public ResponseEntity<SuccessResponse> createComment(
            @Valid @RequestBody CommentCreateRequestDto commentCreateRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        CommentCreateResponseDto commentCreateResponseDto = commentService.createComment(commentCreateRequestDto,
                customUserDetails.getMember().getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.of(CommentSuccessCode.COMMENT_CREATED, commentCreateResponseDto));


    }
}

