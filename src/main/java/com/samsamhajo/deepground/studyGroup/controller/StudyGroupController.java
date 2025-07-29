package com.samsamhajo.deepground.studyGroup.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.global.utils.GlobalLogger;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.dto.*;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupCommentService;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupInviteService;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupService;
import com.samsamhajo.deepground.studyGroup.success.StudyGroupSuccessCode;
import com.samsamhajo.deepground.member.entity.Member;

import jakarta.validation.Valid;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-group")
public class StudyGroupController {
  private final StudyGroupService studyGroupService;

  @GetMapping("/{studyGroupId}")
  public ResponseEntity<SuccessResponse<?>> getStudyGroupDetail(
      @PathVariable Long studyGroupId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails
  ) {
    GlobalLogger.info("스터디 그룹 상세 조회 요청", studyGroupId);
    Member member = customUserDetails.getMember();

    var response = studyGroupService.getStudyGroupDetail(studyGroupId, member.getId());

    return ResponseEntity
        .status(StudyGroupSuccessCode.READ_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.READ_SUCCESS, response));
  }

  @GetMapping("/{studyGroupId}/participants")
  public ResponseEntity<SuccessResponse<List<ParticipantSummaryDto>>> getParticipantSummaries(
          @PathVariable Long studyGroupId
  ) {
    GlobalLogger.info("스터디 참여자 요약 정보 조회", studyGroupId);

    List<ParticipantSummaryDto> response =
            studyGroupService.getParticipantSummaries(studyGroupId);

    return ResponseEntity
            .status(StudyGroupSuccessCode.READ_SUCCESS.getStatus())
            .body(SuccessResponse.of(StudyGroupSuccessCode.READ_SUCCESS, response));
  }

  @GetMapping("/search")
  public ResponseEntity<SuccessResponse<?>> searchStudyGroups(
      @ModelAttribute StudyGroupSearchRequest request
  ) {
    GlobalLogger.info("스터디 목록 검색 요청", request.getKeyword(), request.getGroupStatus(), request.getTechStackNames());

    var response = studyGroupService.searchStudyGroups(request);
    return ResponseEntity
        .status(StudyGroupSuccessCode.SEARCH_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.READ_SUCCESS, response));
  }

  @PostMapping
  public ResponseEntity<SuccessResponse<StudyGroupCreateResponse>> createStudyGroup(
      @RequestBody @Valid StudyGroupCreateRequest request,
      @AuthenticationPrincipal CustomUserDetails customUserDetails
  ) {
    Member member = customUserDetails.getMember();
    GlobalLogger.info("스터디 생성 요청", member.getEmail(), request.getTitle());

    StudyGroupCreateResponse response = studyGroupService.createStudyGroup(request, member);

    return ResponseEntity
        .status(StudyGroupSuccessCode.CREATE_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.CREATE_SUCCESS, response));
  }

  @GetMapping("/joined")
  public ResponseEntity<SuccessResponse<List<StudyGroupParticipationResponse>>> getMyParticipatedGroups(
      @AuthenticationPrincipal CustomUserDetails customUserDetails
  ) {
    Member member = customUserDetails.getMember();
    GlobalLogger.info("참가 중인 스터디 목록 조회", member.getId());

    List<StudyGroupParticipationResponse> response =
        studyGroupService.getStudyGroupsByMember(member.getId());

    return ResponseEntity
        .status(StudyGroupSuccessCode.READ_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.READ_SUCCESS, response));

  }

    @GetMapping("/my")
    public ResponseEntity<SuccessResponse<?>> getMyStudyGroups (
        @AuthenticationPrincipal CustomUserDetails customUserDetails
  ){
      Member member = customUserDetails.getMember();
      GlobalLogger.info("사용자 생성 스터디 목록 조회 요청", member.getId());

      var response = studyGroupService.findMyStudyGroups(member.getId());

      return ResponseEntity
          .status(StudyGroupSuccessCode.READ_SUCCESS.getStatus())
          .body(SuccessResponse.of(StudyGroupSuccessCode.READ_SUCCESS, response));
    }

  @DeleteMapping("/{studyGroupId}")
  public ResponseEntity<SuccessResponse<?>> deleteStudyGroup(
      @PathVariable Long studyGroupId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails
  ) {
    Member requester = customUserDetails.getMember();
    GlobalLogger.info("스터디 소프트 삭제 요청", studyGroupId, requester.getEmail());

    studyGroupService.softDeleteStudyGroup(studyGroupId, requester);

    return ResponseEntity
        .status(StudyGroupSuccessCode.DELETE_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.DELETE_SUCCESS));
  }

    @PatchMapping("/{studyGroupId}")
    public ResponseEntity<SuccessResponse<?>> updateStudyGroup(
            @PathVariable Long studyGroupId,
            @RequestBody @Valid StudyGroupUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Member member = customUserDetails.getMember();
        GlobalLogger.info("스터디 수정 요청", member.getEmail(), studyGroupId);
        var response = studyGroupService.updateStudyGroup(studyGroupId, request, member);
        return ResponseEntity
                .status(StudyGroupSuccessCode.UPDATE_SUCCESS.getStatus())
                .body(SuccessResponse.of(StudyGroupSuccessCode.UPDATE_SUCCESS, response));
    }
}
