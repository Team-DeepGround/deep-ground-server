package com.samsamhajo.deepground.qna.question.Dto;

import com.samsamhajo.deepground.member.entity.Member;
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
    private String imageUrl;

    public static QuestionSummaryDto of(Question q, List<String> techStacks, int answerCount,List<String> mediaUrl, Member member) {
        String imageUrl = null; // 기본값은 null
        if (member.getMemberProfile() != null) {
            imageUrl = member.getMemberProfile().getProfileImage();
        }
        return new QuestionSummaryDto(q.getId(), q.getTitle(), member , q.getQuestionStatus(), techStacks, answerCount, q.getCreatedAt().toLocalDate(), mediaUrl, imageUrl);
    }

    public QuestionSummaryDto(Long questionId, String title, Member member , QuestionStatus questionStatus , List<String> techStacks, int answerCount, LocalDate createdAt, List<String> mediaUrl, String imageUrl) {
        this.questionId = questionId;
        this.title = title;
        this.memberId = member.getId();
        this.nickname = member.getNickname();
        this.status = questionStatus;
        this.techStacks = techStacks;
        this.answerCount = answerCount;
        this.createdAt = createdAt;
        this.mediaUrl = mediaUrl;
        this.imageUrl = (member.getMemberProfile() != null) ? member.getMemberProfile().getProfileImage() : null;
    }
}

