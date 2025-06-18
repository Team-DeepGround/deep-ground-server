package com.samsamhajo.deepground.studyGroup.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.global.utils.GlobalLogger;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCommentRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCommentResponse;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupInviteRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupKickRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupMemberSummary;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupParticipationResponse;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupReplyRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupReplyResponse;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupSearchRequest;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupCommentService;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupDeleteService;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupInviteService;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupJoinService;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupKickService;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupMemberQueryService;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupReplyService;
import com.samsamhajo.deepground.studyGroup.service.StudyGroupService;
import com.samsamhajo.deepground.studyGroup.success.StudyGroupSuccessCode;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCreateRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCreateResponse;

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
  private final StudyGroupJoinService studyGroupJoinService;
  private final StudyGroupDeleteService deleteService;
  private final StudyGroupInviteService inviteService;
  private final MemberRepository memberRepository;
  private final StudyGroupKickService studyGroupKickService;
  private final StudyGroupMemberQueryService queryService;
  private final StudyGroupReplyService replyService;
  private final StudyGroupCommentService commentService;

  @GetMapping("/{studyGroupId}")
  public ResponseEntity<SuccessResponse<?>> getStudyGroupDetail(
      @PathVariable Long studyGroupId
  ) {
    GlobalLogger.info("스터디 그룹 상세 조회 요청", studyGroupId);

    var response = studyGroupService.getStudyGroupDetail(studyGroupId);

    return ResponseEntity
        .status(StudyGroupSuccessCode.READ_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.READ_SUCCESS, response));
  }

  @GetMapping("/search")
  public ResponseEntity<SuccessResponse<?>> searchStudyGroups(
      @ModelAttribute StudyGroupSearchRequest request
  ) {
    GlobalLogger.info("스터디 목록 검색 요청", request.getKeyword(), request.getGroupStatus(), request.getTechTags());

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


  /**
   * 스터디 그룹 참가 요청 API
   *
   * @param studyGroupId 요청 대상 스터디 그룹 ID
   */
  @PostMapping("/{studyGroupId}/join")
  public ResponseEntity<SuccessResponse<?>> requestJoin(
      @PathVariable Long studyGroupId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails
  ) {
    Member member = customUserDetails.getMember();
    GlobalLogger.info("스터디 참가 요청", member.getEmail(), "스터디 ID:", studyGroupId);

    studyGroupJoinService.requestToJoin(member, studyGroupId);

    return ResponseEntity
        .status(StudyGroupSuccessCode.REQUEST_JOIN_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.REQUEST_JOIN_SUCCESS));
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

    deleteService.softDeleteStudyGroup(studyGroupId, requester);

    return ResponseEntity
        .status(StudyGroupSuccessCode.DELETE_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.DELETE_SUCCESS));
  }

  @PostMapping("/invite/{memberId}")
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

  @DeleteMapping("/kick")
  public ResponseEntity<SuccessResponse<?>> kickMember(
      @RequestBody @Valid StudyGroupKickRequest request,
      @RequestAttribute("member") Member requester
  ) {
    GlobalLogger.info("스터디 강퇴 요청", requester.getEmail(), request.getStudyGroupId(), request.getTargetMemberId());

    studyGroupKickService.kickMember(request, requester);

    return ResponseEntity
        .status(StudyGroupSuccessCode.UPDATE_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.UPDATE_SUCCESS));
  }

  @GetMapping("/{studyGroupId}/members")
  public ResponseEntity<SuccessResponse<List<StudyGroupMemberSummary>>> getStudyGroupMembers(
      @PathVariable Long studyGroupId
  ) {
    GlobalLogger.info("스터디 참여자 목록 조회", studyGroupId);

    List<StudyGroupMemberSummary> response = queryService.getAcceptedMembers(studyGroupId);

    return ResponseEntity
        .status(StudyGroupSuccessCode.READ_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.READ_SUCCESS, response));
  }

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

    StudyGroupReplyResponse response = replyService.writeReply(member, request);
    return ResponseEntity
        .status(StudyGroupSuccessCode.CREATE_SUCCESS.getStatus())
        .body(SuccessResponse.of(StudyGroupSuccessCode.CREATE_SUCCESS, response));
  }
}