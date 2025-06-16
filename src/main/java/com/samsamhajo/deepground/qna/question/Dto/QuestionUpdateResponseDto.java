package com.samsamhajo.deepground.qna.question.Dto;

import com.samsamhajo.deepground.qna.question.entity.QuestionTag;
import lombok.Getter;

import java.util.List;

@Getter
public class QuestionUpdateResponseDto {

    private Long questionId;
    private String title;
    private String content;
    private Long memberId;
    private List<String> techStacks;

    public QuestionUpdateResponseDto(Long questionId, String title, String content, Long memberId, List<String> techStacks) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.techStacks = techStacks;
    }
    public static QuestionUpdateResponseDto of(Long questionId, String title, String content, Long memberId, List<String> techStacks) {
        return new QuestionUpdateResponseDto(questionId, title, content, memberId, techStacks);
    }
}
