package com.samsamhajo.deepground.qna.question.repository;

import com.samsamhajo.deepground.qna.question.entity.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTagRepository extends JpaRepository<QuestionTag, Long> {

    void deleteAllByQuestionId(Long questionId);
}
