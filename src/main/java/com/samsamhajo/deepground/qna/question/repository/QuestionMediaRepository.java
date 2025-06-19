package com.samsamhajo.deepground.qna.question.repository;

import com.samsamhajo.deepground.qna.question.entity.QuestionMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionMediaRepository extends JpaRepository<QuestionMedia, Long> {

    List<QuestionMedia> findAllByQuestionId(Long questionId);

    void deleteAllByQuestionId(Long questionId);

    List<QuestionMedia> findByQuestion_Id(Long questionId);
}
