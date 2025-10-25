package com.samsamhajo.deepground.support.dto;

import java.time.LocalDateTime;

public record InquiryAnswerResponse(
        Long id,
        Long inquiryId,
        Long adminId,
        String adminNickname,
        String content,
        LocalDateTime createdAt
) {}
