package com.samsamhajo.deepground.support.repository;

import com.samsamhajo.deepground.support.entity.Inquiry;
import com.samsamhajo.deepground.support.entity.InquiryAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryAnswerRepository extends JpaRepository<InquiryAnswer, Long> {
    List<InquiryAnswer> findByInquiryOrderByIdAsc(Inquiry inquiry);
}
