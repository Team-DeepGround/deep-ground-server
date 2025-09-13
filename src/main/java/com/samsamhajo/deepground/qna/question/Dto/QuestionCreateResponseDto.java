package com.samsamhajo.deepground.qna.question.Dto;

import lombok.Getter;

import java.util.List;

@Getter
public class QuestionCreateResponseDto {

    private Long questionId;
    private String title;
    private String content;
    private Long memberId;
    private List<String> techStacks;
    private List<String> mediaUrls;

    public QuestionCreateResponseDto(Long questionId, String title, String content, Long memberId, List<String> techStacks, List<String> mediaUrls) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.techStacks = techStacks;
        this.mediaUrls = mediaUrls;
    }
    public static QuestionCreateResponseDto of(Long questionId, String title, String content, Long memberId, List<String> techStacks, List<String> mediaUrls) {
        return new QuestionCreateResponseDto(questionId, title, content, memberId, techStacks, mediaUrls);
    }
}
