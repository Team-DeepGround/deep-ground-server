package com.samsamhajo.deepground.qna.question.Dto;

import com.samsamhajo.deepground.qna.question.entity.Question;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class QuestionSummaryDto {
    private Long questionId;
    private String title;
    private Long memberId;
    private List<String> techStacks;
    private int answerCount;
    private LocalDate createdAt;
    private List<String> mediaUrls;

    public static QuestionSummaryDto of(Question q, List<String> techStacks, int answerCount,List<String> mediaUrls) {
        return new QuestionSummaryDto(q.getId(), q.getTitle(), q.getMember().getId(), techStacks, answerCount, q.getCreatedAt().toLocalDate(), mediaUrls);
    }

    public QuestionSummaryDto(Long questionId, String title, Long memberId, List<String> techStacks, int answerCount, LocalDate createdAt, List<String> mediaUrls) {
        this.questionId = questionId;
        this.title = title;
        this.memberId = memberId;
        this.techStacks = techStacks;
        this.answerCount = answerCount;
        this.createdAt = createdAt;
        this.mediaUrls = mediaUrls;
    }
}

