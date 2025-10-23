package com.samsamhajo.deepground.qna.question.repository;

import com.samsamhajo.deepground.qna.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findByMemberId(Long memberId, Pageable pageable);
    Page<Question> findAllByIsDeletedFalse(Pageable pageable);

}

