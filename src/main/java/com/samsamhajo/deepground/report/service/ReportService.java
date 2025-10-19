package com.samsamhajo.deepground.report.service;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.exception.FeedErrorCode;
import com.samsamhajo.deepground.feed.feed.exception.FeedException;
import com.samsamhajo.deepground.feed.feed.repository.FeedRepository;
import com.samsamhajo.deepground.feed.feed.service.FeedService;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.exception.MemberErrorCode;
import com.samsamhajo.deepground.member.exception.MemberException;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.exception.QuestionErrorCode;
import com.samsamhajo.deepground.qna.question.exception.QuestionException;
import com.samsamhajo.deepground.qna.question.repository.QuestionRepository;
import com.samsamhajo.deepground.qna.question.service.QuestionService;
import com.samsamhajo.deepground.report.dto.ReportRequest;
import com.samsamhajo.deepground.report.dto.ReportResponse;
import com.samsamhajo.deepground.report.entity.Report;
import com.samsamhajo.deepground.report.enums.AIReviewResult;
import com.samsamhajo.deepground.report.enums.ReportTargetType;
import com.samsamhajo.deepground.report.exception.ReportErrorCode;
import com.samsamhajo.deepground.report.exception.ReportException;
import com.samsamhajo.deepground.report.repository.ReportRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final StudyGroupMemberRepository studyGroupMemberRepository;
    private final ReportPostAIClient aiClient;
    private final FeedService feedService;

    public ReportResponse createReport(ReportRequest request, Long reporterId) {
        Member reporter = memberRepository.findById(reporterId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 중복 신고 검증
        boolean alreadyReported = reportRepository.existsByReporterIdAndTargetTypeAndTargetId(reporterId, request.targetType(), request.targetId());

        if (alreadyReported) {
            throw new ReportException(ReportErrorCode.DUPLICATE_REPORT);
        }

        Long targetId = request.targetId();
        ReportTargetType targetType = request.targetType();

        Member reportedMember = null;
        boolean isAutoBanned = false;
        AIReviewResult result = AIReviewResult.PENDING;
        boolean isProcessed = false;
        String actionTaken = "관리자에게 위임";

        if (targetType == ReportTargetType.FEED) {
            Feed feed = feedRepository.findById(targetId)
                    .orElseThrow(() -> new FeedException(FeedErrorCode.FEED_NOT_FOUND));

            reportedMember = feed.getMember();

            var aiResult = aiClient.reviewFeed(request.reason(), request.content(), feed.getContent());
            isAutoBanned = aiResult.getResult() == AIReviewResult.ACCEPTED;
            result = aiResult.getResult();

            if (isAutoBanned) {
                feedService.deleteFeed(feed.getId());
                actionTaken = "피드 삭제";
                isProcessed = true;
            }
            if (aiResult.getResult() == AIReviewResult.REJECTED) {
                actionTaken = "피드 유지";
                isProcessed = true;
            }
        }

        if (targetType == ReportTargetType.QUESTION) {
            Question question = questionRepository.findById(targetId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.QUESTION_NOT_FOUND));
            reportedMember = question.getMember();

            var aiResult = aiClient.reviewQuestion(request.reason(), request.content(), question.getContent());
            isAutoBanned = aiResult.getResult() == AIReviewResult.ACCEPTED;
            result = aiResult.getResult();

            if (isAutoBanned) {
                questionService.deleteQuestion(question.getId());
                actionTaken = "피드 삭제";
                isProcessed = true;
            }
            if (aiResult.getResult() == AIReviewResult.REJECTED) {
                actionTaken = "피드 유지";
                isProcessed = true;
            }
        }

        if (targetType == ReportTargetType.MEMBER) {
            // 스터디 그룹 내 멤버인지 검증
            reportedMember = memberRepository.findById(targetId)
                    .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

            boolean isStudyGroupMember = studyGroupMemberRepository.existsById(targetId);
            if (!isStudyGroupMember) {
                throw new MemberException(MemberErrorCode.MEMBER_NOT_IN_STUDY_GROUP);
            }
        }

        Report report = Report.of(
                targetType,
                targetId,
                request.reason(),
                request.content(),
                isAutoBanned,
                reporter,
                reportedMember,
                result,
                isProcessed,
                actionTaken
        );

        reportRepository.save(report);

        return ReportResponse.from(report);
    }
}
