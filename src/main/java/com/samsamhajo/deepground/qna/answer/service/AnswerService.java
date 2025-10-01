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
    public Long deleteAnswer(Long answerId, Long memberId) {

        Question question = commonValidation.QuestionValidation(answerId);
        Answer answer = commonValidation.AnswerValidation(answerId);

        if (!answer.getMember().getId().equals(memberId)) {
            throw new AnswerException(AnswerErrorCode.ANSWER_MEMBER_MISMTACH);
        } else {
            answerLikeRepository.deleteAllByAnswerId(answerId);
            answerRepository.deleteById(answer.getId());
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
    public List<AnswerCreateResponseDto> getAnswersByQuestionId(Long questionId) {
        List<Answer> answers = answerRepository.findAllByQuestionIdWithMember(questionId);
        //질문에 달린 답변이 없으면 빈 List반환
        if(answers.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> answerIds = answers.stream()
                .map(Answer::getId)
                .collect(Collectors.toList());

        List<AnswerMedia> medias = answerMediaRepository.findAllByAnswerIdIn(answerIds);

        //Media를 answerId를 기준으로 grouping 처리
        Map<Long, List<String>> mediaUrl = medias.stream()
                .collect(Collectors.groupingBy(
                        media -> media.getAnswer().getId(),
                        Collectors.mapping(AnswerMedia::getMediaUrl, Collectors.toList())
                ));

        return answers.stream()
                .map(answer -> {
                    List<String> mediaUrls = mediaUrl.getOrDefault(answer.getId(), Collections.emptyList());
                    return AnswerCreateResponseDto.of(answer, mediaUrls);
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
