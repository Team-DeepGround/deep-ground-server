package com.samsamhajo.deepground.qna.answer.repository;

import com.samsamhajo.deepground.qna.answer.entity.AnswerLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerLikeRepository extends JpaRepository<AnswerLike, Long> {

    Optional<AnswerLike> findByAnswerIdAndMemberId(Long answerId, Long memberId);

    void deleteAllByAnswerId(Long answerId);

    boolean existsByAnswerIdAndMemberId(Long answerId, Long memberId);

    int countByAnswerId(Long answerId);
}
