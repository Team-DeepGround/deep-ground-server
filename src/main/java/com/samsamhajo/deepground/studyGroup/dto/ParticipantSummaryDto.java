package com.samsamhajo.deepground.studyGroup.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantSummaryDto {
    private Long memberId;
    private Long profileId;
    private String nickname;
    private String profileImage;
}
