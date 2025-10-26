package com.samsamhajo.deepground.support.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.support.dto.InquiryCreateRequest;
import com.samsamhajo.deepground.support.dto.InquiryResponse;
import com.samsamhajo.deepground.support.dto.InquirySummaryResponse;
import com.samsamhajo.deepground.support.service.InquiryService;
import com.samsamhajo.deepground.support.success.InquirySuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/support/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping
    public ResponseEntity<SuccessResponse<InquiryResponse>> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody InquiryCreateRequest req
    ) {
        InquiryResponse response = inquiryService.create(userDetails.getMember().getId(), req);
        return ResponseEntity
                .status(InquirySuccessCode.CREATE_SUCCESS.getStatus())
                .body(SuccessResponse.of(InquirySuccessCode.CREATE_SUCCESS, response));
    }

    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<List<InquirySummaryResponse>>> myList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<InquirySummaryResponse> response = inquiryService.findMyInquiries(userDetails.getMember().getId());
        return ResponseEntity
                .status(InquirySuccessCode.GET_INQUIRY.getStatus())
                .body(SuccessResponse.of(InquirySuccessCode.GET_INQUIRY, response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<InquiryResponse>> myDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        InquiryResponse response = inquiryService.getMyInquiry(userDetails.getMember().getId(), id);
        return ResponseEntity
                .status(InquirySuccessCode.GET_INQUIRY.getStatus())
                .body(SuccessResponse.of(InquirySuccessCode.GET_INQUIRY, response));
    }
}
