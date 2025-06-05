// 1. 필요한 import
import * as github from '@actions/github';
import * as core from '@actions/core';
import { getReviewerGroups } from './reviewerUtils'; // 그룹 로딩 함수
import { IReviewer } from './types'; // 타입 정의
import { sendDiscordMessage } from './discord'; // ✅ 디스코드 알림용 함수

const githubClient = github.getOctokit(process.env.GITHUB_TOKEN!);

// 2. 그룹 기반 리뷰어 선택
function selectReviewersFromSameGroup(prAuthor: string): IReviewer[] {
    const groups = getReviewerGroups();
    const groupA = groups.groupA;
    const groupB = groups.groupB;

    const isAuthorInA = groupA.some(r => r.githubName === prAuthor);
    const targetGroup = isAuthorInA ? groupA : groupB;

    // 자신 제외한 같은 그룹의 나머지 인원
    const reviewers = targetGroup.filter(r => r.githubName !== prAuthor);

    if (reviewers.length === 0) {
        throw new Error(`같은 그룹 내에 PR 작성자를 제외한 리뷰어가 없습니다.`);
    }

    return reviewers;
}

//3.main 함수
async function main() {
    const pr = github.context.payload.pull_request;
    if (!pr) {
        core.setFailed('이 워크플로우는 pull_request 이벤트에서만 실행됩니다.');
        return;
    }

    const prCreator = pr.user.login;
    const reviewers = selectReviewersFromSameGroup(prCreator);

    // GitHub 리뷰어 요청
    await githubClient.rest.pulls.requestReviewers({
        owner: github.context.repo.owner,
        repo: github.context.repo.repo,
        pull_number: pr.number,
        reviewers: reviewers.map(r => r.githubName),
    });

    // Discord 알림
    await sendDiscordMessage(reviewers);
}

// 4. 실행
main().catch(err => core.setFailed(err.message));

