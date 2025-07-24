package com.samsamhajo.deepground.studyGroup.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.global.utils.GlobalLogger;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupKickRequest;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupAdminService;
import com.samsamhajo.deepground.studyGroup.success.StudyGroupSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-group")
public class StudyGroupAdminController {

  private final StudyGroupAdminService adminService;

  @GetMapping("/{studyGroupId}/admin")
  public ResponseEntity<SuccessResponse<?>> getAdminInfo(
      @PathVariable Long studyGroupId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails
  ) {
    Member member = customUserDetails.getMember();

    var response = adminService.getAdminView(member, studyGroupId);
    return ResponseEntity
        .status(StudyGroupSuccessCode.READ_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.READ_SUCCESS, response));
  }

  @PostMapping("/{studyGroupId}/accept/{targetMemberId}")
  public ResponseEntity<SuccessResponse<?>> acceptMember(
      @PathVariable Long studyGroupId,
      @PathVariable Long targetMemberId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails
  ) {
    Member requester = customUserDetails.getMember();

    adminService.acceptMember(studyGroupId, targetMemberId, requester);

    return ResponseEntity
        .status(StudyGroupSuccessCode.UPDATE_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.UPDATE_SUCCESS));
  }

  @DeleteMapping("/{studyGroupId}/kick/{targetMemberId}")
  public ResponseEntity<SuccessResponse<?>> kickMember(
      @PathVariable Long studyGroupId,
      @PathVariable Long targetMemberId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails
  ) {
    Member requester = customUserDetails.getMember();
    GlobalLogger.info("스터디 강퇴 요청", requester.getEmail(), studyGroupId, targetMemberId);

    StudyGroupKickRequest request = new StudyGroupKickRequest(studyGroupId, targetMemberId);
    adminService.kickMember(request, requester);

    return ResponseEntity
        .status(StudyGroupSuccessCode.UPDATE_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.UPDATE_SUCCESS));
  }
}

