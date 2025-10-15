package com.samsamhajo.deepground.qna.answer.dto;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import com.samsamhajo.deepground.qna.comment.dto.CommentDTO;
import com.samsamhajo.deepground.qna.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class AnswerDetailDto {

    private String answerContent;
    private Long questionId;
    private Long memberId;
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
        this.memberId = member.getId();
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
