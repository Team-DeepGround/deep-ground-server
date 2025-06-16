package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupReplyRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupReplyResponse;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupComment;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupReply;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupCommentRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyGroupReplyService {
  private final StudyGroupReplyRepository replyRepository;
  private final StudyGroupCommentRepository commentRepository;

  @Transactional
  public StudyGroupReplyResponse writeReply(Member writer, StudyGroupReplyRequest request) {
    StudyGroupComment parentComment = commentRepository.findById(request.getCommentId())
        .orElseThrow(() -> new IllegalArgumentException(request.getCommentId() + "가 존재하지 않습니다."));

    StudyGroupReply reply = StudyGroupReply.of(parentComment, writer, request.getContent());
    StudyGroupReply saved = replyRepository.save(reply);

    return StudyGroupReplyResponse.builder()
        .replyId(saved.getId())
        .parentCommentId(parentComment.getId())
        .writerNickname(writer.getNickname())
        .content(saved.getContent())
        .createdAt(saved.getCreatedAt())
        .build();
  }
}
