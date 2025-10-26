package com.samsamhajo.deepground.qna.question.repository;

import com.samsamhajo.deepground.qna.question.entity.QuestionMedia;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionMediaRepository extends JpaRepository<QuestionMedia, Long> {

    List<QuestionMedia> findAllByQuestionId(Long questionId);

//    @Query("SELECT qm FROM QuestionMedia qm WHERE qm.question.id = :questionId AND qm.deleted = false")
//    List<QuestionMedia> findAllByQuestionId(@Param("questionId") Long questionId);

    List<QuestionMedia> findAllByQuestionIdAndDeletedFalse(Long questionId);

    void deleteAllByQuestionId(Long questionId);

    Optional<QuestionMedia> findByMediaUrl(String mediaUrl);


}
