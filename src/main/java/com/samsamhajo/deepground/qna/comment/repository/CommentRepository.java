package com.samsamhajo.deepground.qna.comment.repository;

import com.samsamhajo.deepground.qna.answer.entity.Answer;
import com.samsamhajo.deepground.qna.comment.entity.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByAnswerId(Long answerId);
    void deleteAllByAnswerId(Long answerId);

    @Query("SELECT a FROM Comment a JOIN FETCH a.member WHERE a.answer.id = :answerId AND a.deleted = false")
    List<Comment> findAllByAnswerIdWithMember(@Param("answerId") Long answerId);
}

