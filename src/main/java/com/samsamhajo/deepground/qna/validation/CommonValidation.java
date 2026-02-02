package com.samsamhajo.deepground.qna.validation;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.exception.MemberErrorCode;
import com.samsamhajo.deepground.member.exception.MemberException;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import com.samsamhajo.deepground.qna.answer.entity.AnswerLike;
import com.samsamhajo.deepground.qna.answer.exception.AnswerErrorCode;
import com.samsamhajo.deepground.qna.answer.exception.AnswerException;
import com.samsamhajo.deepground.qna.answer.repository.AnswerLikeRepository;
import com.samsamhajo.deepground.qna.answer.repository.AnswerRepository;
import com.samsamhajo.deepground.qna.comment.entity.Comment;
import com.samsamhajo.deepground.qna.comment.exception.CommentErrorCode;
import com.samsamhajo.deepground.qna.comment.exception.CommentException;
import com.samsamhajo.deepground.qna.comment.repository.CommentRepository;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.exception.QuestionErrorCode;
import com.samsamhajo.deepground.qna.question.exception.QuestionException;
import com.samsamhajo.deepground.qna.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonValidation {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CommentRepository commentRepository;
    private final AnswerLikeRepository answerLikeRepository;

    //Member 공통 Validation 코드

    /**
     * 즉 member라는 객체를 하나 만들건데 memberRepository.findById(우리가 controller에서 받았던 memberId로 DB에서 회원을 찾았을 때
     * 없다면 ? MEMBER_NOT_FOUND라는 에러를 발생해주세요! 즉 회원가입이 된 회원인지 아닌지 검증을 하는 로직이다.
     */
    public Member MemberValidation(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        return member;
    }

    public Question QuestionValidation(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.QUESTION_NOT_FOUND));
        return question;
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

    public AnswerLike LikeValidation(Long answerId, Long memberId) {
        AnswerLike answerLike = answerLikeRepository.findByAnswerIdAndMemberId(answerId, memberId)
                .orElseThrow(() -> new AnswerException(AnswerErrorCode.ANSWER_LIKE_NOT_FOUND));
        return answerLike;
    }


}
