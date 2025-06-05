package com.samsamhajo.deepground.qna.answer.repository;

import com.samsamhajo.deepground.qna.answer.entity.AnswerMedia;
import com.samsamhajo.deepground.qna.question.entity.QuestionMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerMediaRepository extends JpaRepository<AnswerMedia, Long> {
    List<AnswerMedia> findAllByAnswerId(Long answerId);
    void deleteAllByAnswerId(Long answerId);
}
