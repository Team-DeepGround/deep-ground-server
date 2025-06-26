package com.samsamhajo.deepground.qna.question.repository;

import com.samsamhajo.deepground.qna.question.entity.QuestionMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionMediaRepository extends JpaRepository<QuestionMedia, Long> {

    List<QuestionMedia> findAllByQuestionId(Long questionId);

    void deleteAllByQuestionId(Long questionId);

    List<QuestionMedia> findByQuestionId(Long questionId);

    Optional<QuestionMedia> findFirstByQuestionId(Long questionId);

    Optional<QuestionMedia> findByMediaUrl(String mediaUrl);


}
