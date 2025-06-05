package com.samsamhajo.deepground.studyGroup.controller;

import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupAdminViewService;
import com.samsamhajo.deepground.studyGroup.success.StudyGroupSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
  private final MemberRepository memberRepository;

  // TODO: 스프링 시큐리티 구현 완료시, memberId 제외하고 RequestAttribute 도입 고려
  @GetMapping("/{studyGroupId}/admin/{memberId}")
  public ResponseEntity<SuccessResponse<?>> getAdminInfo(
      @PathVariable Long studyGroupId,
      @PathVariable Long memberId
  ) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

    var response = adminViewService.getAdminView(member, studyGroupId);
    return ResponseEntity
        .status(StudyGroupSuccessCode.READ_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.READ_SUCCESS, response));
  }
}

