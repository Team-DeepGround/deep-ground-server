package com.samsamhajo.deepground.studyGroup.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.global.utils.GlobalLogger;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCommentRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCommentResponse;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupReplyRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupReplyResponse;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupCommentService;
import com.samsamhajo.deepground.studyGroup.success.StudyGroupSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-group")
public class StudyGroupCommentController {
  private final StudyGroupCommentService commentService;


  @PostMapping("/comments")
  public ResponseEntity<SuccessResponse<StudyGroupCommentResponse>> writeComment(
      @RequestBody @Valid StudyGroupCommentRequest request,
      @AuthenticationPrincipal CustomUserDetails customUserDetails
  ) {
    Member member = customUserDetails.getMember();
    GlobalLogger.info("스터디 댓글 작성 요청", member.getEmail(), request.getStudyGroupId());

    StudyGroupCommentResponse response = commentService.writeComment(request, member.getId());

    return ResponseEntity
        .status(StudyGroupSuccessCode.COMMENT_CREATE_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.COMMENT_CREATE_SUCCESS, response));
  }

  @PostMapping("/replies")
  public ResponseEntity<SuccessResponse<StudyGroupReplyResponse>> writeReply(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestBody @Valid StudyGroupReplyRequest request
  ) {
    Member member = customUserDetails.getMember();

    StudyGroupReplyResponse response = commentService.writeReply(member, request);
    return ResponseEntity
        .status(StudyGroupSuccessCode.CREATE_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.CREATE_SUCCESS, response));
  }

}
