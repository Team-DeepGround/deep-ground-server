package com.samsamhajo.deepground.support.dto;

import com.samsamhajo.deepground.support.entity.InquiryStatus;

import java.time.LocalDateTime;

public record InquirySummaryResponse(
        Long id,
        String title,
        InquiryStatus status,
        LocalDateTime createdAt
) {}
