package com.samsamhajo.deepground.communityPlace.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.communityPlace.dto.request.CreateReviewDto;
import com.samsamhajo.deepground.communityPlace.dto.response.ReviewResponseDto;
import com.samsamhajo.deepground.communityPlace.exception.CommunityPlaceSuccessCode;
import com.samsamhajo.deepground.communityPlace.service.CommunityPlaceService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/communityplace")
@RequiredArgsConstructor
public class CommunityPlaceController {

    private final CommunityPlaceService communityPlaceService;

    /**
     *
     * @param createReviewDto : SpecificAddress, 별점, 리뷰 내용을 포함하고 있는 DTO
     * @param customUserDetails : Member 인증
     * @return : ReviewResponseDto를 통해 값이 잘 들어갔는지 확인
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> createReview(
            @Valid @ModelAttribute CreateReviewDto createReviewDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        ReviewResponseDto reviewResponseDto = communityPlaceService.createReview(createReviewDto, customUserDetails.getMember().getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.of(CommunityPlaceSuccessCode.REVIEW_CREATED, reviewResponseDto));

    }
}
