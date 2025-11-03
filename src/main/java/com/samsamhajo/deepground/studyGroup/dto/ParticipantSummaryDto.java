package com.samsamhajo.deepground.studyGroup.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantSummaryDto {
    private UUID memberPublicId;
    private UUID profilePublicId;
    private String nickname;
    private String profileImage;
}
