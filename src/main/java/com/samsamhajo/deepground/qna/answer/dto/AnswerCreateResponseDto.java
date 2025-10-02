package com.samsamhajo.deepground.qna.answer.dto;

import com.samsamhajo.deepground.qna.answer.entity.Answer;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class AnswerCreateResponseDto {

    private Long questionId;
    private Long memberId;
    private String answerContent;
    private Long answerId;
    private int likeCount;
    private List<String> mediaUrls;
    private String nickname;
    private LocalDateTime createdAt;


    public AnswerCreateResponseDto(String answerContent, Long questionId, Long memberId, Long answerId, int likeCount, List<String> mediaUrls, String nickname,  LocalDateTime createdAt) {
        this.answerContent = answerContent;
        this.questionId = questionId;
        this.memberId = memberId;
        this.answerId = answerId;
        this.likeCount = likeCount;
        this.mediaUrls = mediaUrls;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }

    public static AnswerCreateResponseDto of(Answer answer, List<String> mediaUrls) {
        String answerContent = answer.getAnswerContent();
        Long questionId = answer.getQuestion().getId();
        Long memberId = answer.getMember().getId();
        Long answerId = answer.getId();
        int likeCount = answer.getAnswerLikeCount();
        String nickname = answer.getMember().getNickname();
        LocalDateTime createdAt = answer.getCreatedAt();

        return new AnswerCreateResponseDto(answerContent, questionId, memberId, answerId, likeCount, mediaUrls, nickname,  createdAt);
    }

}