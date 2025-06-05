package com.samsamhajo.deepground.studyGroup.controller;

import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupReplyRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupReplyResponse;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupReplyService;
import com.samsamhajo.deepground.studyGroup.success.StudyGroupSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-group/replies")
public class StudyGroupReplyController {

  private final StudyGroupReplyService replyService;
  private final MemberRepository memberRepository;

  @PostMapping("/{memberId}")
  public ResponseEntity<SuccessResponse<StudyGroupReplyResponse>> writeReply(
      @PathVariable Long memberId,
      @RequestBody @Valid StudyGroupReplyRequest request
  ) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

    StudyGroupReplyResponse response = replyService.writeReply(member, request);
    return ResponseEntity
        .status(StudyGroupSuccessCode.CREATE_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.CREATE_SUCCESS, response));
  }
}
