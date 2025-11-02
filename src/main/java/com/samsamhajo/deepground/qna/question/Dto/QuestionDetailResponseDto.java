package com.samsamhajo.deepground.qna.question.Dto;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.qna.answer.dto.AnswerCreateResponseDto;
import com.samsamhajo.deepground.qna.answer.dto.AnswerDetailDto;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.entity.QuestionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class QuestionDetailResponseDto {

    private Long questionId;
    private String title;
    private String content;
    private UUID publicId;
    private UUID profilePublicId;
    private String nickname;
    private List<String> techStacks;
    private int answerCount;
    private QuestionStatus questionStatus;
    private List<String> mediaUrl;
    private LocalDateTime createdAt;
    private String imageUrl;

    private List<AnswerDetailDto> answers;

    public QuestionDetailResponseDto(
            Question question,
            Member member,
            List<String> techStacks,
            QuestionStatus questionStatus,
            List<String> mediaUrl,
            UUID profilePublicId,
            String imageUrl,
            List<AnswerDetailDto> answers

    ) {
        this.questionId = question.getId();
        this.title = question.getTitle();
        this.content = question.getContent();
        this.publicId = question.getMember().getPublicId();
        this.profilePublicId = question.getMember().getMemberProfile().getProfilePublicId();
        this.nickname = member.getNickname();
        this.techStacks = techStacks;
        this.answerCount = question.getAnswerCount();
        this.questionStatus = questionStatus;
        this.mediaUrl = mediaUrl;
        this.createdAt = question.getCreatedAt();
        this.imageUrl = imageUrl;
        this.answers = answers;// 추가
    }

    public static QuestionDetailResponseDto of(
            Question question,
            Member member,
            List<String> techStacks,
            QuestionStatus questionStatus,
            List<String> mediaUrl,
            String imageUrl,
            List<AnswerDetailDto> answers
    ) {
        return new QuestionDetailResponseDto(
                question,
                member,
                techStacks,
                questionStatus,
                mediaUrl,
                member.getMemberProfile().getProfilePublicId(),
                member.getMemberProfile().getProfileImage(),
                answers
        );
    }
}
