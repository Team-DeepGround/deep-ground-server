package com.samsamhajo.deepground.member.controller;


import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.member.Dto.MemberProfileDto;
import com.samsamhajo.deepground.member.Dto.PresenceDto;
import com.samsamhajo.deepground.member.exception.MemberSuccessCode;
import com.samsamhajo.deepground.member.exception.ProfileSuccessCode;
import com.samsamhajo.deepground.member.service.PresenceService;
import com.samsamhajo.deepground.member.service.MemberService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final PresenceService presenceService;

    @PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<MemberProfileDto>> editMemberProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("profile") @Valid MemberProfileDto memberProfileDto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

        Long memberId = userDetails.getMember().getId();
        MemberProfileDto profile = memberService.editMemberProfile(memberId, memberProfileDto, profileImage);

        return ResponseEntity.ok(SuccessResponse.of(ProfileSuccessCode.PROFILE_SUCCESS_CODE, profile));
    }

    @GetMapping("/online")
    public ResponseEntity<SuccessResponse<List<PresenceDto>>> getOnlineMembers(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();

        List<PresenceDto> response = presenceService.isOnlineMembers(memberId);
        return ResponseEntity
                .ok(SuccessResponse.of(MemberSuccessCode.ONLINE_SUCCESS_CODE, response));
    }

    @PostMapping(value = "/profile/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<MemberProfileDto>> createProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("profile") @Valid MemberProfileDto memberProfileDto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

        Long memberId = userDetails.getMember().getId();
        MemberProfileDto profile = memberService.createProfile(memberId, memberProfileDto, profileImage);

        return ResponseEntity.ok(SuccessResponse.of(ProfileSuccessCode.PROFILE_CREATE_SUCCESS, profile));
    }

    @GetMapping("/profile/me")
    public ResponseEntity<SuccessResponse<MemberProfileDto>> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getId();
        MemberProfileDto profile = memberService.getMyProfile(memberId);

        return ResponseEntity.ok(SuccessResponse.of(ProfileSuccessCode.GET_MY_PROFILE_SUCCESS, profile));
    }
  
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<SuccessResponse<MemberProfileDto>> getUserProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long profileId){

        Long memberId = userDetails.getMember().getId();
        MemberProfileDto profile = memberService.getUserProfile(memberId,profileId);
        return ResponseEntity
                .ok(SuccessResponse.of(ProfileSuccessCode.GET_SUCCESS_PROFILE, profile));
    }


}

