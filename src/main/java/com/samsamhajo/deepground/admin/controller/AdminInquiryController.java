package com.samsamhajo.deepground.admin.controller;

import com.samsamhajo.deepground.admin.service.AdminInquiryService;
import com.samsamhajo.deepground.admin.success.AdminSuccessCode;
import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.support.dto.InquiryAnswerRequest;
import com.samsamhajo.deepground.support.dto.InquiryAnswerResponse;
import com.samsamhajo.deepground.support.dto.InquiryResponse;
import com.samsamhajo.deepground.support.dto.InquirySummaryResponse;
import com.samsamhajo.deepground.support.entity.InquiryStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/inquiries")
@RequiredArgsConstructor
public class AdminInquiryController {

    private final AdminInquiryService adminInquiryService;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<InquirySummaryResponse>>> list(
            @RequestParam(required = false) InquiryStatus status
    ) {
        List<InquirySummaryResponse> response = adminInquiryService.list(status);
        return ResponseEntity.ok(SuccessResponse.of(AdminSuccessCode.GET_INQUIRY_SUCCESS, response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<InquiryResponse>> detail(@PathVariable Long id) {
        InquiryResponse response = adminInquiryService.detail(id);
        return ResponseEntity.ok(SuccessResponse.of(AdminSuccessCode.GET_INQUIRY_SUCCESS, response));
    }

    @PostMapping("/{id}/answers")
    public ResponseEntity<SuccessResponse<InquiryAnswerResponse>> answer(
            @AuthenticationPrincipal CustomUserDetails admin,
            @PathVariable Long id,
            @Valid @RequestBody InquiryAnswerRequest req
    ) {
        InquiryAnswerResponse response = adminInquiryService.answer(admin.getMember().getId(), id, req);
        return ResponseEntity
                .status(AdminSuccessCode.CREATE_INQUIRY_ANSWER_SUCCESS.getStatus())
                .body(SuccessResponse.of(AdminSuccessCode.CREATE_INQUIRY_ANSWER_SUCCESS, response));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SuccessResponse<Void>> changeStatus(
            @PathVariable Long id,
            @RequestParam InquiryStatus status
    ) {
        adminInquiryService.changeStatus(id, status);
        return ResponseEntity
                .status(AdminSuccessCode.MODIFY_INQUIRY_STATUS_SUCCESS.getStatus())
                .body(SuccessResponse.of(AdminSuccessCode.MODIFY_INQUIRY_STATUS_SUCCESS));
    }
}
