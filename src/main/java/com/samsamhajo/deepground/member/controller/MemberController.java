package com.samsamhajo.deepground.member.controller;



import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.member.Dto.MemberProfileDto;
import com.samsamhajo.deepground.member.exception.ProfileSuccessCode;
import com.samsamhajo.deepground.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PutMapping("/profile")
    public ResponseEntity<SuccessResponse> editMemberProfile(@RequestBody @Valid MemberProfileDto memberprofile,
                                                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getId();
        MemberProfileDto profile = memberService.editMemberProfile(memberId, memberprofile);
        return ResponseEntity
                .ok(SuccessResponse.of(ProfileSuccessCode.PROFILE_SUCCESS_CODE,profile));

    }
}
