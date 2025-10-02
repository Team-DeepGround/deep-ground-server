package com.samsamhajo.deepground.qna.comment.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.notification.entity.data.QNANotificationData;
import com.samsamhajo.deepground.notification.event.NotificationEvent;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import com.samsamhajo.deepground.qna.comment.dto.*;
import com.samsamhajo.deepground.qna.comment.entity.Comment;
import com.samsamhajo.deepground.qna.comment.exception.CommentErrorCode;
import com.samsamhajo.deepground.qna.comment.exception.CommentException;
import com.samsamhajo.deepground.qna.comment.repository.CommentRepository;
import com.samsamhajo.deepground.qna.validation.CommonValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CommonValidation commonValidation;

    @Transactional
    public CommentCreateResponse createComment(CreateCommentRequest createCommentRequest, Long memberId) {

        //Member객체를 통한 Comment생성을 위한 proxy객체 생성
        Member member = memberRepository.getReferenceById(memberId);
        //첫 댓글 작성 시에는 Answer객체 검증
        Answer answer = commonValidation.AnswerValidation(createCommentRequest.getAnswerId());

        Comment comment = Comment.of(
                createCommentRequest.getCommentContent(),
                member,
                answer
        );

        Comment saved = commentRepository.save(comment);

        // 댓글 알림
        Long answerMemberId = answer.getMember().getId();
        if (!answerMemberId.equals(memberId)) {
            eventPublisher.publishEvent(NotificationEvent.of(
                    answerMemberId,
                    QNANotificationData.comment(answer.getQuestion().getId(), comment.getCommentContent())
            ));
        }

        return CommentCreateResponse.changeEntity(saved);
    }
  
    @Transactional
    public UpdateCommentResponseDto updateComment(UpdateCommentRequestDto updateCommentRequestDto, Long memberId) {

        Comment comment = commonValidation.CommentValidation(updateCommentRequestDto.getCommentId());

        if(!comment.getMember().getId().equals(memberId)) {
            throw new CommentException(CommentErrorCode.COMMENT_MEMBER_MISMATCH);
        } else {
            comment.updateCommentContent(updateCommentRequestDto.getCommentContent());
        }

        return UpdateCommentResponseDto.of(
                comment.getCommentContent(),
                comment.getMember().getId(),
                comment.getAnswer().getId(),
                comment.getId()
        );
    }

    @Transactional
    public Long deleteComment(Long commentId, Long memberId) {

        Comment comment = commonValidation.CommentValidation(commentId);

        // 댓글 작성한 MemberId와 API요청을 보낸 MemberId가 다르면 예외처리 & 같으면 삭제처리
        if(!comment.getMember().getId().equals(memberId)) {
            throw new CommentException(CommentErrorCode.COMMENT_MEMBER_MISMATCH);
        } else {
            commentRepository.deleteById(commentId);
        }
        return commentId;
    }

    @Transactional(readOnly = true)
    public List<CommentDetail> getComments(Long answerId, Long memberId) {

        commonValidation.AnswerValidation(answerId);
        List<Comment> comments = commentRepository.findAllByAnswerIdWithMember(answerId);

        return comments.stream()
                .map(comment -> {
                    return CommentDetail.of(comment);
                }).collect(Collectors.toList());
    }
}


