package com.samsamhajo.deepground.qna.answer.repository;

import com.samsamhajo.deepground.qna.answer.entity.AnswerMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerMediaRepository extends JpaRepository<AnswerMedia, Long> {
    List<AnswerMedia> findAllByAnswerId(Long answerId);
    List<AnswerMedia> findAllByAnswerIdIn(List<Long> answerIds);
    void deleteAllByAnswerId(Long answerId);
    Optional<AnswerMedia> findByMediaUrl(String mediaUrl);
}
