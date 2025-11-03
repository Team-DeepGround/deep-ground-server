package com.samsamhajo.deepground.qna.answer.dto;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import com.samsamhajo.deepground.qna.question.entity.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class AnswerDetailDto {

    private String answerContent;
    private Long questionId;
    private UUID publicId;
    private UUID profilePublicId;
    private Long answerId;
    private int likeCount;
    private List<String> mediaUrl;
    private String nickname;
    private LocalDateTime createdAt;
    private String imageUrl;

    public AnswerDetailDto(
            Question question,
            Answer answer,
            Member member,
            List<String> mediaUrl,
            LocalDateTime createdAt,
            String imageUrl

    ) {
        this.answerContent = answer.getAnswerContent();
        this.questionId = question.getId();
        this.publicId = member.getPublicId();
        this.profilePublicId = member.getMemberProfile().getProfilePublicId();
        this.answerId = answer.getId();
        this.likeCount = answer.getAnswerLikeCount();
        this.mediaUrl = mediaUrl;
        this.nickname = member.getNickname();
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
    }

    public static AnswerDetailDto of(Question question, Answer answer, Member member,List<String> mediaUrl,LocalDateTime createdAt,  String imageUrl) {
        return new AnswerDetailDto(question, answer, member, mediaUrl, createdAt, imageUrl);
    }
}
