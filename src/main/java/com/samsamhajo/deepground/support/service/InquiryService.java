package com.samsamhajo.deepground.support.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.support.dto.*;
import com.samsamhajo.deepground.support.entity.Inquiry;
import com.samsamhajo.deepground.support.repository.InquiryAnswerRepository;
import com.samsamhajo.deepground.support.repository.InquiryRepository;
import jakarta.persistence.EntityNotFoundException;
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
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

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
                .orElseThrow(() -> new EntityNotFoundException("문의가 존재하지 않습니다."));
        if (!inquiry.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("본인이 작성한 문의만 조회할 수 있습니다.");
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
