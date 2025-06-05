package com.samsamhajo.deepground.qna.comment.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import com.samsamhajo.deepground.qna.answer.exception.AnswerErrorCode;
import com.samsamhajo.deepground.qna.answer.exception.AnswerException;
import com.samsamhajo.deepground.qna.answer.repository.AnswerRepository;
import com.samsamhajo.deepground.qna.comment.dto.CommentCreateRequestDto;
import com.samsamhajo.deepground.qna.comment.dto.CommentCreateResponseDto;
import com.samsamhajo.deepground.qna.comment.entity.Comment;
import com.samsamhajo.deepground.qna.comment.exception.CommentErrorCode;
import com.samsamhajo.deepground.qna.comment.exception.CommentException;
import com.samsamhajo.deepground.qna.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    public CommentCreateResponseDto createComment(CommentCreateRequestDto commentCreateRequestDto, Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );

        Answer answer = answerRepository.findById(commentCreateRequestDto.getAnswerId()).orElseThrow(() ->
                new AnswerException(AnswerErrorCode.ANSWER_NOT_FOUND)
        );

        if(!StringUtils.hasText(commentCreateRequestDto.getCommentContent())) {
            throw new CommentException(CommentErrorCode.COMMENT_REQUIRED);
        }

        Comment comment = Comment.of(
                commentCreateRequestDto.getCommentContent(),
                member,
                answer
        );

        Comment saved = commentRepository.save(comment);

        return CommentCreateResponseDto.of(
                saved.getCommentContent(),
                saved.getAnswer().getId(),
                saved.getMember().getId(),
                saved.getId()
        );


    }
}
