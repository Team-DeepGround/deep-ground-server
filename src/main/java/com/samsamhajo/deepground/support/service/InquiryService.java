package com.samsamhajo.deepground.support.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.exception.MemberErrorCode;
import com.samsamhajo.deepground.member.exception.MemberException;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.support.dto.*;
import com.samsamhajo.deepground.support.entity.Inquiry;
import com.samsamhajo.deepground.support.exception.InquiryErrorCode;
import com.samsamhajo.deepground.support.exception.InquiryException;
import com.samsamhajo.deepground.support.repository.InquiryAnswerRepository;
import com.samsamhajo.deepground.support.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.samsamhajo.deepground.support.entity.InquiryStatus.PENDING;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryAnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public InquiryResponse create(Long memberId, InquiryCreateRequest req) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.INVALID_MEMBER_ID));

        Inquiry inquiry = inquiryRepository.save(
                Inquiry.of(member, req.title(), req.content(), PENDING)
        );

        return toDetailDto(inquiry, List.of());
    }

    public List<InquirySummaryResponse> findMyInquiries(Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        return inquiryRepository.findAllByMemberOrderByIdDesc(member).stream()
                .map(i -> new InquirySummaryResponse(i.getId(), i.getTitle(), i.getStatus(), i.getCreatedAt()))
                .toList();
    }

    public InquiryResponse getMyInquiry(Long memberId, Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new InquiryException(InquiryErrorCode.INQUIRY_NOT_FOUND));
        if (!inquiry.getMember().getId().equals(memberId)) {
            throw new InquiryException(InquiryErrorCode.FORBIDDEN_INQUIRY_ACCESS);
        }
        var answers = answerRepository.findByInquiryOrderByIdAsc(inquiry).stream()
                .map(a -> new InquiryAnswerResponse(
                        a.getId(),
                        inquiry.getId(),
                        a.getAdmin().getId(),
                        a.getAdmin().getNickname(),
                        a.getContent(),
                        a.getCreatedAt()
                )).toList();

        return toDetailDto(inquiry, answers);
    }

    private InquiryResponse toDetailDto(Inquiry i, List<InquiryAnswerResponse> answers) {
        return new InquiryResponse(
                i.getId(),
                i.getTitle(),
                i.getContent(),
                i.getStatus(),
                i.getCreatedAt(),
                i.getModifiedAt(),
                answers
        );
    }
}
