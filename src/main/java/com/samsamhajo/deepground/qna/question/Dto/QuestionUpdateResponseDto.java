package com.samsamhajo.deepground.qna.question.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class QuestionUpdateResponseDto {

    private Long questionId;
    private String title;
    private String content;
    private UUID publicId;
    private List<String> techStacks;
    private List<String> mediaUrl;

    public QuestionUpdateResponseDto(Long questionId, String title, String content, UUID publicId, List<String> techStacks, List<String> mediaUrl) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.publicId = publicId;
        this.techStacks = techStacks;
        this.mediaUrl = mediaUrl;
    }
    public static QuestionUpdateResponseDto of(Long questionId, String title, String content, UUID publicId, List<String> techStacks, List<String> mediaUrl) {
        return new QuestionUpdateResponseDto(questionId, title, content, publicId, techStacks, mediaUrl);
    }
}
