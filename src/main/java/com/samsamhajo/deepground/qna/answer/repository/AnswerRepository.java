package com.samsamhajo.deepground.qna.answer.repository;

import com.samsamhajo.deepground.qna.answer.entity.Answer;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("SELECT a FROM Answer a JOIN FETCH a.member WHERE a.question.id = :questionId AND a.deleted = false")
    List<Answer> findAllByQuestionIdWithMember(@Param("questionId") Long questionId);

    List<Answer> findAllByQuestionId(Long questionId);
}