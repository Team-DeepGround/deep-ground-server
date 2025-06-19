package com.samsamhajo.deepground.qna.question.Dto;

import com.samsamhajo.deepground.qna.answer.dto.AnswerCreateResponseDto;
import com.samsamhajo.deepground.qna.question.entity.QuestionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class QuestionDetailResponseDto {

    private Long questionId;
    private String title;
    private String content;
    private Long memberId;
    private String nickname;
    private List<String> techStacks;
    private int answerCount;
    private QuestionStatus questionStatus;
    private List<String> mediaUrls;

    private List<AnswerCreateResponseDto> answers;

    public QuestionDetailResponseDto(
            Long questionId,
            String title,
            String content,
            Long memberId,
            String nickname,
            List<String> techStacks,
            int answerCount,
            QuestionStatus questionStatus,
            List<String> mediaUrls,
            List<AnswerCreateResponseDto> answers  // 추가
    ) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.nickname = nickname;
        this.techStacks = techStacks;
        this.answerCount = answerCount;
        this.questionStatus = questionStatus;
        this.mediaUrls = mediaUrls;
        this.answers = answers;  // 추가
    }

    public static QuestionDetailResponseDto of(
            Long questionId,
            String title,
            String content,
            Long memberId,
            String nickname,
            List<String> techStacks,
            int answerCount,
            QuestionStatus questionStatus,
            List<String> mediaUrls,
            List<AnswerCreateResponseDto> answers
    ) {
        return new QuestionDetailResponseDto(
                questionId,
                title,
                content,
                memberId,
                nickname,
                techStacks,
                answerCount,
                questionStatus,
                mediaUrls,
                answers
        );
    }
}
