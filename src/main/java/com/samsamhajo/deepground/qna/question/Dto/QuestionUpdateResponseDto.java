package com.samsamhajo.deepground.qna.question.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionUpdateResponseDto {

    private Long questionId;
    private String title;
    private String content;
    private Long memberId;
    private List<String> techStacks;
    private List<String> mediaUrl;

    public QuestionUpdateResponseDto(Long questionId, String title, String content, Long memberId, List<String> techStacks, List<String> mediaUrl) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.techStacks = techStacks;
        this.mediaUrl = mediaUrl;
    }
    public static QuestionUpdateResponseDto of(Long questionId, String title, String content, Long memberId, List<String> techStacks, List<String> mediaUrl) {
        return new QuestionUpdateResponseDto(questionId, title, content, memberId, techStacks, mediaUrl);
    }
}
