package com.samsamhajo.deepground.communityPlace.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.communityPlace.dto.request.AddressDto;
import com.samsamhajo.deepground.communityPlace.dto.request.CreateReviewDto;
import com.samsamhajo.deepground.communityPlace.dto.response.ReviewResponseDto;
import com.samsamhajo.deepground.communityPlace.service.CommunityPlaceService;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.qna.question.exception.QuestionSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/communityplace")
@RequiredArgsConstructor
public class CommunityPlaceController {

    private final CommunityPlaceService communityPlaceService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> createReview(
            @Valid @ModelAttribute CreateReviewDto createReviewDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        createReviewDto.setImages(images);
        ReviewResponseDto reviewResponseDto = communityPlaceService.createReview(createReviewDto, customUserDetails.getMember().getId());
        AddressDto dto = createReviewDto.getAddress();
        System.out.println("RAW address.latitude: " + dto.getLatitude());
        System.out.println("RAW address.longitude: " + dto.getLongitude());


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.of(QuestionSuccessCode.QUESTION_CREATED, reviewResponseDto));

    }
}
