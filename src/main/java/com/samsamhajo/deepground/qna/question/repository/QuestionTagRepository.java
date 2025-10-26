package com.samsamhajo.deepground.qna.question.repository;

import com.samsamhajo.deepground.qna.question.entity.QuestionTag;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionTagRepository extends JpaRepository<QuestionTag, Long> {

    void deleteAllByQuestionId(Long questionId);

    List<QuestionTag> findByTechStackId(Long techStackId);

    List<QuestionTag> findAllByQuestionId(Long questionId);

    List<QuestionTag> findByTechStackIdAndDeletedFalse(Long techStackId);
}
