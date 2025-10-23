package com.samsamhajo.deepground.qna.answer.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.notification.entity.data.QNANotificationData;
import com.samsamhajo.deepground.notification.event.NotificationEvent;
import com.samsamhajo.deepground.qna.answer.dto.*;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import com.samsamhajo.deepground.qna.answer.entity.AnswerMedia;
import com.samsamhajo.deepground.qna.answer.repository.AnswerLikeRepository;
import com.samsamhajo.deepground.qna.answer.repository.AnswerMediaRepository;
import com.samsamhajo.deepground.qna.answer.repository.AnswerRepository;
import com.samsamhajo.deepground.qna.answer.exception.AnswerErrorCode;
import com.samsamhajo.deepground.qna.answer.exception.AnswerException;
import com.samsamhajo.deepground.qna.comment.repository.CommentRepository;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.validation.CommonValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    private final AnswerMediaService answerMediaService;
    private final AnswerMediaRepository answerMediaRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CommonValidation commonValidation;
    private final AnswerLikeRepository answerLikeRepository;
    private final CommentRepository commentRepository;


    @Transactional
    public AnswerCreateResponseDto createAnswer(AnswerCreateRequestDto answerCreateRequestDto, Long memberId) {

        Member member = memberRepository.getReferenceById(memberId);
        Question question = commonValidation.QuestionValidation(answerCreateRequestDto.getQuestionId());

        Answer answer = Answer.of(
                answerCreateRequestDto.getAnswerContent(),
                member,
                question
        );

        Answer saved = answerRepository.save(answer);
        //TODO : Event를 통한 Question과 책임 분리 고려
        question.incrementAnswerCount();
        List<String> mediaUrl = createAnswerMedia(answerCreateRequestDto, answer);

        // 답변 알림
        eventPublisher.publishEvent(NotificationEvent.of(
                question.getMember().getId(),
                QNANotificationData.answer(answer)
        ));

        return AnswerCreateResponseDto.of(answer, mediaUrl);
    }

    @Transactional
    public Long deleteAnswer(Long answerId, Long memberId, Long questionId) {

        Question question = commonValidation.QuestionValidation(questionId);
        Answer answer = commonValidation.AnswerValidation(answerId);

        if (!answer.getMember().getId().equals(memberId)) {
            throw new AnswerException(AnswerErrorCode.ANSWER_MEMBER_MISMTACH);
        } else {
//            commentRepository.deleteAllByAnswerId(answerId);
//            answerLikeRepository.deleteAllByAnswerId(answerId);
//            answerMediaRepository.deleteAllByAnswerId(answerId);
//            answerRepository.deleteById(answer.getId());
            answer.softDelete();
        }
        //TODO event를 통해 Question, Answer 책임 분리
        question.decrementAnswerCount();

        return answer.getId();
    }

    @Transactional
    public AnswerUpdateResponseDto updateAnswer(AnswerUpdateRequestDto answerUpdateRequestDto, Long memberId) {

        Member member = memberRepository.getReferenceById(memberId);
        Answer answer = commonValidation.AnswerValidation(answerUpdateRequestDto.getAnswerId());

        if(!answer.getMember().getId().equals(memberId)) {
            throw new AnswerException(AnswerErrorCode.ANSWER_MEMBER_MISMTACH);
        } else {
            answer.updateAnswer(answerUpdateRequestDto.getAnswerContent());
        }

        answerMediaService.deleteAnswerMedia(answer.getId());
        List<String> mediaUrl = updateAnswerMedia(answerUpdateRequestDto, answer);

        return AnswerUpdateResponseDto.of(
                answer.getAnswerContent(),
                answer.getQuestion().getId(),
                answer.getId(),
                member.getId(),
                mediaUrl
        );
    }

    // Image 업로드 메서드
    private List<String> createAnswerMedia(AnswerCreateRequestDto answerCreateRequestDto, Answer answer) {
       return answerMediaService.createAnswerMedia(answer, answerCreateRequestDto.getImages());
    }

    // Image 수정 메서드
    private List<String> updateAnswerMedia(AnswerUpdateRequestDto answerUpdateRequestDto, Answer answer) {
        return answerMediaService.createAnswerMedia(answer, answerUpdateRequestDto.getImages());
    }

    // Question에 달린 답변 수 조회
    public int countAnswersByQuestionId(Long questionId) {
        Question question = commonValidation.QuestionValidation(questionId);
        return question.getAnswerCount();
    }

    // 질문에 달린 답변 목록 조회
    @Transactional(readOnly = true)
    public List<AnswerDetailDto> getAnswersByQuestionId(Long questionId) {
        Question question = commonValidation.QuestionValidation(questionId);
        List<Answer> answers = answerRepository.findAllByQuestionIdWithMember(questionId);

        if (answers.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> answerIds = answers.stream()
                .filter(answer -> !answer.isDeleted())
                .map(Answer::getId)
                .collect(Collectors.toList());

        List<AnswerMedia> medias = answerMediaRepository.findAllByAnswerIdInAndIsDeletedFalse(answerIds);

        Map<Long, List<String>> mediaUrlMap = medias.stream()
                .collect(Collectors.groupingBy(
                        media -> media.getAnswer().getId(),
                        Collectors.mapping(AnswerMedia::getMediaUrl, Collectors.toList())
                ));

        return answers.stream()
                .map(answer -> {
                    // 3. 각 답변(answer)에 맞는 실제 작성자(author)를 가져옴
                    Member author = answer.getMember();

                    // 4. 프로필이 null일 경우를 대비한 방어 코드 추가
                    String imageUrl = (author.getMemberProfile() != null)
                            ? author.getMemberProfile().getProfileImage()
                            : null;

                    List<String> mediaUrls = mediaUrlMap.getOrDefault(answer.getId(), Collections.emptyList());

                    // 5. DTO 생성자에 올바른 인자들을 전달
                    return AnswerDetailDto.of(
                            question,
                            answer,
                            author,
                            mediaUrls,
                            answer.getCreatedAt(), // 답변의 생성 시간 전달
                            imageUrl
                    );
                }).collect(Collectors.toList());
    }

    // 특정 답변 수정을 위한 조회 로직
    @Transactional(readOnly = true)
    public AnswerEditResponseDto getAnswerEditInfo(Long answerId, Long memberId) {
        Answer answer =commonValidation.AnswerValidation(answerId);

        List<String> mediaUrls = answerMediaRepository.findAllByAnswerId(answerId)
                .stream()
                .map(AnswerMedia::getMediaUrl)
                .collect(Collectors.toList());

        return AnswerEditResponseDto.of(answer, mediaUrls);
    }
}
