package com.samsamhajo.deepground.support.repository;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.support.entity.Inquiry;
import com.samsamhajo.deepground.support.entity.InquiryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findAllByMemberOrderByIdDesc(Member member);
    boolean existsByIdAndMember(Long id, Member member);
    List<Inquiry> findAllByStatusOrderByIdDesc(InquiryStatus status);
}
