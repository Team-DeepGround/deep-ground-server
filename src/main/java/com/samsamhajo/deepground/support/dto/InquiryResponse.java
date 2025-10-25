package com.samsamhajo.deepground.support.dto;

import com.samsamhajo.deepground.support.entity.InquiryStatus;

import java.time.LocalDateTime;
import java.util.List;

public record InquiryResponse(
        Long id,
        String title,
        String content,
        InquiryStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<InquiryAnswerResponse> answers
) {}
