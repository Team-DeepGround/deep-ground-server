package com.samsamhajo.deepground.qna.comment.controller;

import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.qna.comment.dto.CommentCreateRequestDto;
import com.samsamhajo.deepground.qna.comment.dto.CommentCreateResponseDto;
import com.samsamhajo.deepground.qna.comment.exception.CommentSuccessCode;
import com.samsamhajo.deepground.qna.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<SuccessResponse> createComment(
            @Valid @ModelAttribute CommentCreateRequestDto commentCreateRequestDto
    ) {
        Long memberId = 1L;
        CommentCreateResponseDto commentCreateResponseDto = commentService.createComment(commentCreateRequestDto, memberId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.of(CommentSuccessCode.COMMENT_CREATED, commentCreateResponseDto));

    }
}
