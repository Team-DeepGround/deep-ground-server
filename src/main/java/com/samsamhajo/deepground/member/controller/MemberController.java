package com.samsamhajo.deepground.member.controller;



import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.member.Dto.MemberProfileDto;
import com.samsamhajo.deepground.member.exception.ProfileSuccessCode;
import com.samsamhajo.deepground.member.service.MemberService;
import jakarta.validation.Valid;
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

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<MemberProfileDto>> editMemberProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("profile") @Valid MemberProfileDto memberProfileDto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

        Long memberId = userDetails.getMember().getId();
        MemberProfileDto profile = memberService.editMemberProfile(memberId, memberProfileDto, profileImage);

        return ResponseEntity.ok(SuccessResponse.of(ProfileSuccessCode.PROFILE_SUCCESS_CODE, profile));
    }
}
