package com.samsamhajo.deepground.qna.question.Dto;

import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.entity.QuestionStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class QuestionSummaryDto {
    private Long questionId;
    private String title;
    private Long memberId;
    private String nickname;
    private QuestionStatus status;
    private List<String> techStacks;
    private int answerCount;
    private LocalDate createdAt;
    private List<String> mediaUrl;

    public static QuestionSummaryDto of(Question q, List<String> techStacks, int answerCount,List<String> mediaUrl) {
        return new QuestionSummaryDto(q.getId(), q.getTitle(), q.getMember().getId(), q.getMember().getNickname(), q.getQuestionStatus(), techStacks, answerCount, q.getCreatedAt().toLocalDate(), mediaUrl);
    }

    public QuestionSummaryDto(Long questionId, String title, Long memberId, String nickname, QuestionStatus questionStatus ,List<String> techStacks, int answerCount, LocalDate createdAt, List<String> mediaUrl) {
        this.questionId = questionId;
        this.title = title;
        this.memberId = memberId;
        this.nickname = nickname;
        this.status = questionStatus;
        this.techStacks = techStacks;
        this.answerCount = answerCount;
        this.createdAt = createdAt;
        this.mediaUrl = mediaUrl;
    }
}

