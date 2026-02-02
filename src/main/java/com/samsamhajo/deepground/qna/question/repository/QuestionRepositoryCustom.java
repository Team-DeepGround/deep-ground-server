package com.samsamhajo.deepground.qna.question.repository;

import com.samsamhajo.deepground.qna.question.Dto.QuestionSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface QuestionRepositoryCustom {
    Page<QuestionSummaryDto> findQuestionSummaries(Pageable pageable);
}
