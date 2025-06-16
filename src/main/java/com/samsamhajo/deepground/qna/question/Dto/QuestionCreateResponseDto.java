package com.samsamhajo.deepground.qna.question.Dto;

import com.samsamhajo.deepground.qna.question.entity.QuestionTag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class QuestionCreateResponseDto {

    private Long questionId;
    private String title;
    private String content;
    private Long memberId;
    private List<String> techStacks;

    public QuestionCreateResponseDto(Long questionId, String title, String content, Long memberId, List<String> techStacks) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.techStacks = techStacks;
    }
    public static QuestionCreateResponseDto of(Long questionId, String title, String content, Long memberId, List<String> techStacks) {
        return new QuestionCreateResponseDto(questionId, title, content, memberId, techStacks);
    }
}
