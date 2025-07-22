package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCommentRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupCommentResponse;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupReplyRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupReplyResponse;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupComment;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupReply;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupCommentRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupReplyRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudyGroupCommentService {

  private final StudyGroupCommentRepository commentRepository;
  private final StudyGroupRepository studyGroupRepository;
  private final StudyGroupReplyRepository replyRepository;
  private final MemberRepository memberRepository;

  @Transactional
  public StudyGroupCommentResponse writeComment(StudyGroupCommentRequest requestDto, Long memberId) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

    StudyGroup studyGroup = studyGroupRepository.findById(requestDto.getStudyGroupId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디 그룹입니다."));

    StudyGroupComment comment = StudyGroupComment.of(studyGroup, member, requestDto.getContent());
    StudyGroupComment saved = commentRepository.save(comment);

    return StudyGroupCommentResponse.from(
        saved.getId(),
        member.getNickname(),
        saved.getContent(),
        saved.getCreatedAt()
    );
  }

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
