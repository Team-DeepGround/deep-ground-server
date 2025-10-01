package com.samsamhajo.deepground.qna.question.Dto;

import com.samsamhajo.deepground.qna.question.entity.Question;
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
    public static QuestionCreateResponseDto of(Question question, List<String> techStacks, List<String> mediaUrls) {
        Long questionId = question.getId();
        String title = question.getTitle();
        String content = question.getContent();
        Long memberId = question.getMember().getId();

        return new QuestionCreateResponseDto(questionId, title, content, memberId, techStacks, mediaUrls);
    }
}
