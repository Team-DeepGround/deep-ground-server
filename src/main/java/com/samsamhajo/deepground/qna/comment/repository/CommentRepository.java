package com.samsamhajo.deepground.qna.comment.repository;

import com.samsamhajo.deepground.qna.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}