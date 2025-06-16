package com.samsamhajo.deepground.qna.answer.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import com.samsamhajo.deepground.qna.answer.entity.AnswerLike;
import com.samsamhajo.deepground.qna.answer.exception.AnswerErrorCode;
import com.samsamhajo.deepground.qna.answer.exception.AnswerException;
import com.samsamhajo.deepground.qna.answer.repository.AnswerLikeRepository;
import com.samsamhajo.deepground.qna.answer.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerLikeService {

    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;
    private final AnswerLikeRepository answerLikeRepository;

    @Transactional
    public void answerLike(Long answerId, Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Answer answer = answerRepository.findById(answerId).orElseThrow(
                () -> new AnswerException(AnswerErrorCode.ANSWER_NOT_FOUND));

        if(answerLikeRepository.existsByAnswerIdAndMemberId(answerId, memberId)) {
            throw new AnswerException(AnswerErrorCode.ANSWER_ALREADY_LIKED);
        }

        AnswerLike answerLike = AnswerLike.of(member, answer);
        answerLikeRepository.save(answerLike);
        answer.incrementAnswerLikeCount();
        }

        @Transactional
    public void answerUnLike(Long answerId, Long memberId) {

        Answer answer = answerRepository.findById(answerId).orElseThrow(
                () -> new AnswerException(AnswerErrorCode.ANSWER_NOT_FOUND));

        AnswerLike answerLike = answerLikeRepository.findByAnswerIdAndMemberId(answerId, memberId).orElseThrow(
                () -> new AnswerException(AnswerErrorCode.ANSWER_LIKE_NOT_FOUND));

        answerLikeRepository.delete(answerLike);
        answer.decrementAnswerLikeCount();
    }

    public void deleteAllByAnswerId(Long answerId) {
        answerLikeRepository.deleteAllByAnswerId(answerId);
    }
}
