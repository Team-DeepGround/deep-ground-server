package com.samsamhajo.deepground.qna.question.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.answer.dto.AnswerCreateResponseDto;
import com.samsamhajo.deepground.qna.answer.dto.AnswerDetailDto;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import com.samsamhajo.deepground.qna.answer.repository.AnswerRepository;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService{

    private final QuestionRepository questionRepository;
    private final QuestionMediaService questionMediaService;
    private final QuestionTagRepository questionTagRepository;
    private final AnswerRepository answerRepository;
    private final TechStackRepository techStackRepository;
    private final QuestionTagService questionTagService;
    private final AnswerService answerService;
    private final QuestionTagService tagService;
    private final QuestionMediaRepository questionMediaRepository;
    private final CommonValidation commonValidation;
    private final MemberRepository memberRepository;

    //Question 생성 메서드
    @Transactional
    /**
     * public QuestionCreateResponseDTO createQuestion(QuestionCreateRequestDto questionCreateRequestDto, Long memberId) ->
     * QuestionCreateRequestDto와 memberId를 받아서 QuestionCreateResponseDto로 반환해주는 createQuestion 즉 질문을 생성해주는 함수입니다. 라고 말하고 있는 것이다.
     */
    public QuestionCreateResponseDto createQuestion(QuestionCreateRequestDto questionCreateRequestDto, Long memberId) {
        // member 검증 로직
       Member member = commonValidation.MemberValidation(memberId);

       // 질문이라는 객체를 만들건데 아까 우리가 받았던 questionCreateRequestDto에 담긴 제목과 내용, 작성자로 질문이라는 객체를 만들어주세요!
        Question question = Question.of(
                questionCreateRequestDto.getTitle(),
                questionCreateRequestDto.getContent(),
                member
        );

        // Question saved = Question이라는 객체를 saved라는 변수로 활용하기 위해서 questionRepository (즉 질문DB에 저장해주세요)
        Question saved = questionRepository.save(question);
        List<String> mediaUrl = createQuestionMedia(questionCreateRequestDto, question);
        questionTagService.createQuestionTag(question, questionCreateRequestDto.getTechStacks());

        // QuestionCreateResponseDto을 saved(즉 우리가 저장한 Question(질문)으로 조립해서 반환해주세요!)
        return QuestionCreateResponseDto.of(
                saved,
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
//            List<Answer> answers = answerRepository.findAllByQuestionId(questionId);
//            answerRepository.deleteAll(answers);
//            questionTagRepository.deleteAllByQuestionId(questionId);
//            questionMediaService.deleteQuestionMedia(questionId);
//            questionRepository.deleteById(questionId);
            question.softDelete();
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
                question.getMember().getPublicId(),
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
                question.getMember().getPublicId()
        );
    }

    //Question 리스트 조회 메서드
    @Transactional(readOnly = true)
    public QuestionListResponseDto getQuestions(Pageable pageable) {

        Page<QuestionSummaryDto> page =
                questionRepository.findQuestionSummaries(pageable);

        return QuestionListResponseDto.of(
                page.getContent(),
                page.getTotalPages()
        );
    }


    //Question 상세 조회 메서드
    @Transactional(readOnly = true)
    public QuestionDetailResponseDto getQuestionDetail(Long questionId, Long memberId) {

//        Question question = commonValidation.QuestionValidation(questionId);
        Question question = questionRepository.findByIdAndDeletedFalse(questionId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.QUESTION_NOT_FOUND));
        Member writeMember = question.getMember();

        List<QuestionMedia> questionMedia = questionMediaRepository.findAllByQuestionIdAndDeletedFalse(question.getId());

        List<String> techStacks = tagService.getStackNamesByQuestionId(questionId);

        List<String> mediaUrl = questionMedia.stream()
                .map(QuestionMedia::getMediaUrl)
                .collect(Collectors.toList());

        List<AnswerDetailDto> answers = answerService.getAnswersByQuestionId(questionId);

        return QuestionDetailResponseDto.of(
                question,
                writeMember,
                techStacks,
                question.getQuestionStatus(),
                mediaUrl,
                writeMember.getMemberProfile().getProfileImage(),
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
                    Member member = commonValidation.MemberValidation(question.getMember().getId());
                    List<String> techStacks = questionTagService.getStackNamesByQuestionId(question.getId());
                    int answerCount = answerService.countAnswersByQuestionId(question.getId());
                    List<String> mediaUrls = questionMediaRepository.findAllByQuestionId(question.getId())
                            .stream()
                            .map(qm -> qm.getMediaUrl())
                            .toList();

                    return QuestionSummaryDto.of(question, techStacks, answerCount, mediaUrls, member);
                }).toList();

        return QuestionListResponseDto.of(summaries, questionPage.getTotalPages());
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        questionTagRepository.deleteAllByQuestionId(questionId);
        questionMediaService.deleteQuestionMedia(questionId);
        questionRepository.deleteById(questionId);

    }


}
