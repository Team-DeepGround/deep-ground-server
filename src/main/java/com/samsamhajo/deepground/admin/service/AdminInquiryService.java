package com.samsamhajo.deepground.admin.service;

import com.samsamhajo.deepground.admin.exception.AdminErrorCode;
import com.samsamhajo.deepground.admin.exception.AdminException;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.support.dto.InquiryAnswerRequest;
import com.samsamhajo.deepground.support.dto.InquiryAnswerResponse;
import com.samsamhajo.deepground.support.dto.InquiryResponse;
import com.samsamhajo.deepground.support.dto.InquirySummaryResponse;
import com.samsamhajo.deepground.support.entity.Inquiry;
import com.samsamhajo.deepground.support.entity.InquiryAnswer;
import com.samsamhajo.deepground.support.entity.InquiryStatus;
import com.samsamhajo.deepground.support.exception.InquiryErrorCode;
import com.samsamhajo.deepground.support.exception.InquiryException;
import com.samsamhajo.deepground.support.repository.InquiryAnswerRepository;
import com.samsamhajo.deepground.support.repository.InquiryRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.samsamhajo.deepground.support.entity.InquiryStatus.ANSWERED;
import static com.samsamhajo.deepground.support.entity.InquiryStatus.IN_PROGRESS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminInquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryAnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    public List<InquirySummaryResponse> list(InquiryStatus status) {
        var list = (status == null)
                ? inquiryRepository.findAll().stream().sorted((a,b) -> Long.compare(b.getId(), a.getId())).toList()
                : inquiryRepository.findAllByStatusOrderByIdDesc(status);

        return list.stream()
                .map(i -> new InquirySummaryResponse(i.getId(), i.getTitle(), i.getStatus(), i.getCreatedAt()))
                .toList();
    }

    public InquiryResponse detail(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new InquiryException(InquiryErrorCode.INQUIRY_NOT_FOUND));

        var answers = answerRepository.findByInquiryOrderByIdAsc(inquiry).stream()
                .map(a -> new InquiryAnswerResponse(
                        a.getId(),
                        inquiry.getId(),
                        a.getAdmin().getId(),
                        a.getAdmin().getNickname(),
                        a.getContent(),
                        a.getCreatedAt()
                )).toList();

        return new InquiryResponse(
                inquiry.getId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getStatus(),
                inquiry.getCreatedAt(),
                inquiry.getModifiedAt(),
                answers
        );
    }

    @Transactional
    public InquiryAnswerResponse answer(Long adminId, Long inquiryId, InquiryAnswerRequest req) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new InquiryException(InquiryErrorCode.INQUIRY_NOT_FOUND));
        Member admin = memberRepository.findById(adminId)
                .orElseThrow(() -> new AdminException(AdminErrorCode.ADMIN_NOT_FOUND));

        // 첫 답변이면 진행중으로, 이후엔 ANSWERED로 마킹하는 식으로 운영 가능
        if (inquiry.getStatus() == InquiryStatus.PENDING) {
            inquiry.changeStatus(IN_PROGRESS);
        }

        InquiryAnswer answer = answerRepository.save(InquiryAnswer.of(inquiry, admin, req.content()));

        inquiry.changeStatus(ANSWERED);

        return new InquiryAnswerResponse(
                answer.getId(),
                inquiry.getId(),
                admin.getId(),
                admin.getNickname(),
                answer.getContent(),
                answer.getCreatedAt()
        );
    }

    @Transactional
    public void changeStatus(Long inquiryId, InquiryStatus status) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new InquiryException(InquiryErrorCode.INQUIRY_NOT_FOUND));

        inquiry.changeStatus(status);
    }

}
