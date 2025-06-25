package com.samsamhajo.deepground.qna.question.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
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
import com.samsamhajo.deepground.techStack.entity.TechStack;
import com.samsamhajo.deepground.techStack.repository.TechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService{

    private final QuestionRepository questionRepository;
    private final QuestionMediaService questionMediaService;
    private final QuestionTagRepository questionTagRepository;
    private final TechStackRepository techStackRepository;
    private final MemberRepository memberRepository;
    private final QuestionTagService questionTagService;
    private final AnswerService answerService;
    private final QuestionTagService tagService;
    private final QuestionMediaRepository questionMediaRepository;

    //질문 생성
    @Transactional
    public QuestionCreateResponseDto createQuestion(QuestionCreateRequestDto questionCreateRequestDto, Long memberId) {
       Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        if(!StringUtils.hasText(questionCreateRequestDto.getTitle())) {
            throw new QuestionException(QuestionErrorCode.QUESTION_TITLE_REQUIRED);
        }

        if(!StringUtils.hasText(questionCreateRequestDto.getContent())) {
            throw new QuestionException(QuestionErrorCode.QUESTION_CONTENT_REQUIRED);
        }

        Question question = Question.of(
                questionCreateRequestDto.getTitle(),
                questionCreateRequestDto.getContent(),
                member
        );

        Question saved = questionRepository.save(question);
        List<String> mediaUrls = createQuestionMedia(questionCreateRequestDto, question);
        questionTagService.createQuestionTag(question, questionCreateRequestDto.getTechStacks());



        return QuestionCreateResponseDto.of(
                saved.getId(),
                saved.getTitle(),
                saved.getContent(),
                memberId,
                questionCreateRequestDto.getTechStacks()
                ,mediaUrls
        );
    }

    @Transactional
    public Long deleteQuestion(Long questionId, Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new QuestionException(QuestionErrorCode.QUESTION_MEMBER_MISMATCH));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.QUESTION_NOT_FOUND));

        if(!question.getMember().getId().equals(memberId)) {
            throw new QuestionException(QuestionErrorCode.QUESTION_MEMBER_MISMATCH);
        }

        questionTagRepository.deleteAllByQuestionId(questionId);
        questionMediaService.deleteQuestionMedia(questionId);
        questionRepository.deleteById(questionId);

        return question.getId();

    }

    @Transactional
    public QuestionUpdateResponseDto updateQuestion(QuestionUpdateRequestDto questionUpdateRequestDto, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.QUESTION_MEMBER_MISMATCH));

        if(!StringUtils.hasText(questionUpdateRequestDto.getTitle())) {
            throw new QuestionException(QuestionErrorCode.QUESTION_TITLE_REQUIRED);
        }

        if(!StringUtils.hasText(questionUpdateRequestDto.getContent())) {
            throw new QuestionException(QuestionErrorCode.QUESTION_CONTENT_REQUIRED);
        }

        Question question = questionRepository.findById(questionUpdateRequestDto.getQuestionId())
                .orElseThrow(()-> new QuestionException(QuestionErrorCode.QUESTION_NOT_FOUND));

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


        QuestionUpdateResponseDto questionUpdateResponseDto = QuestionUpdateResponseDto.of(
                questionUpdateRequestDto.getQuestionId(),
                questionUpdateRequestDto.getTitle(),
                questionUpdateRequestDto.getContent(),
                memberId,
                techStacks
        );

        question.updateQuesiton(questionUpdateRequestDto.getTitle(), questionUpdateRequestDto.getContent());
        questionMediaService.deleteQuestionMedia(question.getId());
        updateQuestionMedia(questionUpdateRequestDto, question);



        return questionUpdateResponseDto;

    }
    private List<String> createQuestionMedia(QuestionCreateRequestDto questionCreateRequestDto, Question question) {
        questionMediaService.createQuestionMedia(question, questionCreateRequestDto.getImages());
        return null;
    }

    private void updateQuestionMedia(QuestionUpdateRequestDto questionUpdateRequestDto, Question question) {
        questionMediaService.createQuestionMedia(question, questionUpdateRequestDto.getImages());
    }

    @Transactional
    public QuestionUpdateStatusResponseDto updateQuestionStatus(QuestionUpdateStatusRequestDto questionUpdateStatusRequestDto, Long memberId, Long questionId) {

        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new QuestionException(QuestionErrorCode.QUESTION_MEMBER_MISMATCH));

        Question question = questionRepository.findById(questionId).orElseThrow(
                () -> new QuestionException(QuestionErrorCode.QUESTION_NOT_FOUND));

        if(!member.getId().equals(memberId)) {
            throw new QuestionException(QuestionErrorCode.QUESTION_MEMBER_MISMATCH);
        }
        question.updateStatus(questionUpdateStatusRequestDto.getStatus());

        return QuestionUpdateStatusResponseDto.of(
                question.getId(),
                question.getQuestionStatus(),
                memberId
        );
    }


    @Transactional(readOnly = true)
    public QuestionListResponseDto getQuestions(Pageable pageable) {
        Page<Question> questionPage = questionRepository.findAll(pageable);

        List<QuestionSummaryDto> summaries = questionPage.stream()
                .map(question -> {
                    List<String> teckStacks = tagService.getStackNamesByQuestionId(question.getId());
                    int answerCount = answerService.countAnswersByQuestionId(question.getId());

                    List<String> mediaUrls = questionMediaRepository.findAllByQuestionId(question.getId()).stream()
                            .map(QuestionMedia::getMediaUrl)
                            .toList();


                    return QuestionSummaryDto.of(question, teckStacks, answerCount, mediaUrls);
                }).toList();

        return QuestionListResponseDto.of(summaries, questionPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public QuestionDetailResponseDto getQuestionDetail(Long questionId, Long memberId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.QUESTION_NOT_FOUND));
        List<QuestionMedia> questionMedia = questionMediaRepository.findAllByQuestionId(questionId);
        int answerCount = answerService.countAnswersByQuestionId(questionId);
        List<String> techStacks = tagService.getStackNamesByQuestionId(questionId);
        List<String> imageUrls = questionMedia.stream()
                .map(QuestionMedia::getMediaUrl)
                .collect(Collectors.toList());

        List<AnswerCreateResponseDto> answers = answerService.getAnswersByQuestionId(questionId);


        return QuestionDetailResponseDto.of(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getMember().getId(),
                question.getMember().getNickname(),
                techStacks,
                answerCount,
                question.getQuestionStatus(),
                imageUrls,
                answers
        );
    }


}
