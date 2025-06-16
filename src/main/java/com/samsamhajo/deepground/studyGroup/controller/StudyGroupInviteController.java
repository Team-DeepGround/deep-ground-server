package com.samsamhajo.deepground.studyGroup.controller;

import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupInviteRequest;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupInviteService;
import com.samsamhajo.deepground.studyGroup.success.StudyGroupSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-group/invite")
public class StudyGroupInviteController {

  private final StudyGroupInviteService inviteService;
  private final MemberRepository memberRepository;

  @PostMapping("/{memberId}")
  public ResponseEntity<SuccessResponse<?>> inviteByEmail(
      @PathVariable Long memberId,
      @RequestBody @Valid StudyGroupInviteRequest request
  ) {
    Member inviter = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

    inviteService.inviteByEmail(inviter, request);
    return ResponseEntity
        .status(StudyGroupSuccessCode.CREATE_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.CREATE_SUCCESS));
  }
}