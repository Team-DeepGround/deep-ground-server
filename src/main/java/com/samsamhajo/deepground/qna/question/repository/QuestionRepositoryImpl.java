package com.samsamhajo.deepground.qna.question.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.samsamhajo.deepground.member.entity.QMember;
import com.samsamhajo.deepground.qna.answer.entity.QAnswer;
import com.samsamhajo.deepground.qna.question.Dto.QuestionSummaryDto;
import com.samsamhajo.deepground.qna.question.entity.QQuestion;
import com.samsamhajo.deepground.qna.question.entity.QQuestionMedia;
import com.samsamhajo.deepground.qna.question.entity.QQuestionTag;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.techStack.entity.QTechStack;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<QuestionSummaryDto> findQuestionSummaries(Pageable pageable) {

        QQuestion q = QQuestion.question;
        QMember m = QMember.member;

        // 1) Question + Member (페이징)
        List<Question> questions = queryFactory
                .selectFrom(q)
                .leftJoin(q.member, m).fetchJoin()
                .where(q.deleted.isFalse())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (questions.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Long> questionIds =
                questions.stream().map(Question::getId).toList();

        // 2) TechStacks
        Map<Long, List<String>> techStackMap =
                getTechStacksByQuestionIds(questionIds);

        // 3) AnswerCount
        Map<Long, Integer> answerCountMap =
                getAnswerCountsByQuestionIds(questionIds);

        // 4) MediaUrls
        Map<Long, List<String>> mediaUrlMap =
                getMediaUrlsByQuestionIds(questionIds);

        // 5) DTO 변환
        List<QuestionSummaryDto> summaries = questions.stream()
                .map(qEntity -> QuestionSummaryDto.of(
                        qEntity,
                        techStackMap.getOrDefault(qEntity.getId(), List.of()),
                        answerCountMap.getOrDefault(qEntity.getId(), 0),
                        mediaUrlMap.getOrDefault(qEntity.getId(), List.of()),
                        qEntity.getMember()
                ))
                .toList();

        // 전체 개수
        long total = queryFactory
                .select(q.count())
                .from(q)
                .where(q.deleted.isFalse())
                .fetchOne();

        return new PageImpl<>(summaries, pageable, total);
    }


    private Map<Long, List<String>> getTechStacksByQuestionIds(List<Long> ids) {
        QQuestionTag qt = QQuestionTag.questionTag;
        QTechStack ts = QTechStack.techStack;

        List<Tuple> results = queryFactory
                .select(qt.question.id, ts.name)
                .from(qt)
                .leftJoin(qt.techStack, ts)
                .where(qt.question.id.in(ids))
                .fetch();

        return results.stream()
                .collect(Collectors.groupingBy(
                        t -> t.get(qt.question.id),
                        Collectors.mapping(
                                t -> t.get(ts.name),
                                Collectors.toList()
                        )
                ));
    }

    private Map<Long, Integer> getAnswerCountsByQuestionIds(List<Long> ids) {
        QAnswer a = QAnswer.answer;

        List<Tuple> results = queryFactory
                .select(a.question.id, a.count())
                .from(a)
                .where(a.question.id.in(ids))
                .groupBy(a.question.id)
                .fetch();

        return results.stream()
                .collect(Collectors.toMap(
                        t -> t.get(a.question.id),
                        t -> t.get(a.count()).intValue()
                ));
    }

    private Map<Long, List<String>> getMediaUrlsByQuestionIds(List<Long> ids) {
        QQuestionMedia qm = QQuestionMedia.questionMedia;

        List<Tuple> results = queryFactory
                .select(qm.question.id, qm.mediaUrl)
                .from(qm)
                .where(qm.question.id.in(ids))
                .fetch();

        return results.stream()
                .collect(Collectors.groupingBy(
                        t -> t.get(qm.question.id),
                        Collectors.mapping(
                                t -> t.get(qm.mediaUrl),
                                Collectors.toList()
                        )
                ));
    }
}
