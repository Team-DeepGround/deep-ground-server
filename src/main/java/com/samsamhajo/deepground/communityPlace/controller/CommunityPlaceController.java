package com.samsamhajo.deepground.communityPlace.controller;

import com.samsamhajo.deepground.communityPlace.entity.CommunityPlaceReview;
import com.samsamhajo.deepground.communityPlace.exception.CommunityPlaceSuccessCode;
import com.samsamhajo.deepground.communityPlace.service.CommunityPlaceService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/communityplace")
@RequiredArgsConstructor
public class CommunityPlaceController {

    private final CommunityPlaceService communityPlaceService;

    @GetMapping("/{specificAddressId}")
    public ResponseEntity<SuccessResponse> selectCommunityPlaceReviewsAndScope(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @PathVariable Long specificAddressId) {
        CommunityPlaceReview reviewData = communityPlaceService.selectCommunityPlaceReviewsAndScope(specificAddressId);
        return ResponseEntity
                .ok(SuccessResponse.of(CommunityPlaceSuccessCode.COMMUNITYPLACE_SUCCESS_SELECT,reviewData));
    }
}