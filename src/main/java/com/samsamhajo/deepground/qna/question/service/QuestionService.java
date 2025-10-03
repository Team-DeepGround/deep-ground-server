package com.samsamhajo.deepground.qna.question.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.qna.answer.dto.AnswerCreateResponseDto;
import com.samsamhajo.deepground.qna.answer.service.AnswerService;
import com.samsamhajo.deepground.qna.question.Dto.*;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.entity.QuestionMedia;
import com.samsamhajo.deepground.qna.question.entity.QuestionTag;
import com.samsamhajo.deepground.qna.question.exception.QuestionErrorCode;
import com.samsamhajo.deepground.qna.question.exception.QuestionException;
import com.samsamhajo.deepground.qna.question.repository.QuestionMediaRepository;
import com.samsamhajo.deepground.qna.question.repository.QuestionRepository;
import com.samsamhajo.deepground.qna.question.repository.QuestionTagRepository;
import com.samsamhajo.deepground.qna.validation.CommonValidation;
import com.samsamhajo.deepground.techStack.entity.TechStack;
import com.samsamhajo.deepground.techStack.repository.TechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService{

    private final QuestionRepository questionRepository;
    private final QuestionMediaService questionMediaService;
    private final QuestionTagRepository questionTagRepository;
    private final TechStackRepository techStackRepository;
    private final QuestionTagService questionTagService;
    private final AnswerService answerService;
    private final QuestionTagService tagService;
    private final QuestionMediaRepository questionMediaRepository;
    private final CommonValidation commonValidation;

    //Question 생성 메서드
    @Transactional
    public QuestionCreateResponseDto createQuestion(QuestionCreateRequestDto questionCreateRequestDto, Long memberId) {
       Member member = commonValidation.MemberValidation(memberId);

        Question question = Question.of(
                questionCreateRequestDto.getTitle(),
                questionCreateRequestDto.getContent(),
                member
        );

        Question saved = questionRepository.save(question);
        List<String> mediaUrl = createQuestionMedia(questionCreateRequestDto, question);
        questionTagService.createQuestionTag(question, questionCreateRequestDto.getTechStacks());

        return QuestionCreateResponseDto.of(
                question,
                questionCreateRequestDto.getTechStacks(),
                mediaUrl
        );
    }

    //Question 삭제 메서드
    @Transactional
    public Long deleteQuestion(Long questionId, Long memberId) {

        Question question = commonValidation.QuestionValidation(questionId);

        if(!question.getMember().getId().equals(memberId)) {
            throw new QuestionException(QuestionErrorCode.QUESTION_MEMBER_MISMATCH);
        } else {
            questionTagRepository.deleteAllByQuestionId(questionId);
            questionMediaService.deleteQuestionMedia(questionId);
            questionRepository.deleteById(questionId);
        }

        return question.getId();
    }

    //Question 수정 메서드
    @Transactional
    public QuestionUpdateResponseDto updateQuestion(QuestionUpdateRequestDto questionUpdateRequestDto, Long memberId) {

        Question question = commonValidation.QuestionValidation(questionUpdateRequestDto.getQuestionId());

        if(!question.getMember().getId().equals(memberId)) {
            throw new QuestionException(QuestionErrorCode.QUESTION_MEMBER_MISMATCH);
        }

        questionTagRepository.deleteAllByQuestionId(question.getId());

        List<String> techStacks = questionUpdateRequestDto.getTechStacks();

        for (String name : techStacks) {
            TechStack techStack = techStackRepository.findByName(name)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태그: " + name));
            QuestionTag questionTag = QuestionTag.of(question, techStack);
            questionTagRepository.save(questionTag);
        }

        question.updateQuesiton(questionUpdateRequestDto.getTitle(), questionUpdateRequestDto.getContent());
        questionMediaService.deleteQuestionMedia(question.getId());
        List<String> mediaUrl = updateQuestionMedia(questionUpdateRequestDto, question);

        return QuestionUpdateResponseDto.of(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getMember().getId(),
                techStacks,
                mediaUrl
        );

    }

    private List<String> createQuestionMedia(QuestionCreateRequestDto questionCreateRequestDto, Question question) {
        return questionMediaService.createQuestionMedia(question, questionCreateRequestDto.getImages());

    }

    private List<String> updateQuestionMedia(QuestionUpdateRequestDto questionUpdateRequestDto, Question question) {
        return questionMediaService.createQuestionMedia(question, questionUpdateRequestDto.getImages());
    }

    //Question 상태 수정 메서드
    @Transactional
    public QuestionUpdateStatusResponseDto updateQuestionStatus(QuestionUpdateStatusRequestDto questionUpdateStatusRequestDto, Long memberId, Long questionId) {

        Member member = commonValidation.MemberValidation(memberId);
        Question question = commonValidation.QuestionValidation(questionId);

        if(!member.getId().equals(memberId)) {
            throw new QuestionException(QuestionErrorCode.QUESTION_MEMBER_MISMATCH);
        } else { question.updateStatus(questionUpdateStatusRequestDto.getStatus());}

        return QuestionUpdateStatusResponseDto.of(
                question.getId(),
                question.getQuestionStatus(),
                memberId
        );
    }

    //Question 리스트 조회 메서드
    @Transactional(readOnly = true)
    public QuestionListResponseDto getQuestions(Pageable pageable) {
        Page<Question> questionPage = questionRepository.findAll(pageable);

        List<QuestionSummaryDto> summaries = questionPage.stream()
                .map(question -> {
                    List<String> teckStacks = tagService.getStackNamesByQuestionId(question.getId());
                    int answerCount = answerService.countAnswersByQuestionId(question.getId());

                    List<String> mediaUrl = questionMediaRepository.findAllByQuestionId(question.getId()).stream()
                            .map(QuestionMedia::getMediaUrl)
                            .toList();


                    return QuestionSummaryDto.of(question, teckStacks, answerCount, mediaUrl);
                }).toList();

        return QuestionListResponseDto.of(summaries, questionPage.getTotalPages());
    }

    //Question 상세 조회 메서드
    @Transactional(readOnly = true)
    public QuestionDetailResponseDto getQuestionDetail(Long questionId, Long memberId) {

        Member member = commonValidation.MemberValidation(memberId);
        Question question = commonValidation.QuestionValidation(questionId);

        List<QuestionMedia> questionMedia = questionMediaRepository.findAllByQuestionId(questionId);

        List<String> techStacks = tagService.getStackNamesByQuestionId(questionId);

        List<String> mediaUrl = questionMedia.stream()
                .map(QuestionMedia::getMediaUrl)
                .collect(Collectors.toList());

        List<AnswerCreateResponseDto> answers = answerService.getAnswersByQuestionId(questionId);

        return QuestionDetailResponseDto.of(
                question,
                member,
                techStacks,
                question.getQuestionStatus(),
                mediaUrl,
                answers
        );
    }

    //member가 작성한 question리스트 조회 메서드
    //TODO : 현재 마이페이지에 내가 조회한 Question이 없는 관계로 추후에 생긴 후 Refactoring 할 예정
    @Transactional(readOnly = true)
    public QuestionListResponseDto getQuestionsByMemberId(Long memberId, Pageable pageable) {
        Page<Question> questionPage = questionRepository.findByMemberId(memberId, pageable);

        List<QuestionSummaryDto> summaries = questionPage.stream()
                .map(question -> {
                    List<String> techStacks = questionTagService.getStackNamesByQuestionId(question.getId());
                    int answerCount = answerService.countAnswersByQuestionId(question.getId());
                    List<String> mediaUrls = questionMediaRepository.findAllByQuestionId(question.getId())
                            .stream()
                            .map(qm -> qm.getMediaUrl())
                            .toList();

                    return QuestionSummaryDto.of(question, techStacks, answerCount, mediaUrls);
                }).toList();

        return QuestionListResponseDto.of(summaries, questionPage.getTotalPages());
    }


}
