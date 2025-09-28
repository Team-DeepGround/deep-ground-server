package com.samsamhajo.deepground.qna.validation;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.exception.MemberErrorCode;
import com.samsamhajo.deepground.member.exception.MemberException;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import com.samsamhajo.deepground.qna.answer.exception.AnswerErrorCode;
import com.samsamhajo.deepground.qna.answer.exception.AnswerException;
import com.samsamhajo.deepground.qna.answer.repository.AnswerRepository;
import com.samsamhajo.deepground.qna.comment.entity.Comment;
import com.samsamhajo.deepground.qna.comment.exception.CommentErrorCode;
import com.samsamhajo.deepground.qna.comment.exception.CommentException;
import com.samsamhajo.deepground.qna.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonValidation {

    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;
    private final CommentRepository commentRepository;

    //Member 공통 Validation 코드
    public Member MemberValidation(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        return member;
    }

    //Answer 공통 Validation 코드
    public Answer AnswerValidation(Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerException(AnswerErrorCode.ANSWER_NOT_FOUND));

        return answer;
    }

    public Comment  CommentValidation(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
        return comment;
    }


}
