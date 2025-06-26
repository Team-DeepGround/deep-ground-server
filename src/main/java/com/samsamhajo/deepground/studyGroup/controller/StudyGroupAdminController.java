package com.samsamhajo.deepground.studyGroup.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupAdminViewService;
import com.samsamhajo.deepground.studyGroup.success.StudyGroupSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-group")
public class StudyGroupAdminController {

  private final StudyGroupAdminViewService adminViewService;

  @GetMapping("/{studyGroupId}/admin")
  public ResponseEntity<SuccessResponse<?>> getAdminInfo(
      @PathVariable Long studyGroupId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails
  ) {
    Member member = customUserDetails.getMember();

    var response = adminViewService.getAdminView(member, studyGroupId);
    return ResponseEntity
        .status(StudyGroupSuccessCode.READ_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.READ_SUCCESS, response));
  }
}

