// 1. 필요한 import
import * as github from '@actions/github';
import * as core from '@actions/core';
import { getAllReviewers } from './reviewerUtils'; // 그룹 로딩 함수
import { IReviewer } from './types'; // 타입 정의
import { sendDiscordMessage } from './discord'; // ✅ 디스코드 알림용 함수

const githubClient = github.getOctokit(process.env.GITHUB_TOKEN!);

// 2. 그룹 기반 리뷰어 선택
function selectRandomReviewers(prAuthor: string, count: number = 2): IReviewer[] {
    const allReviewers = getAllReviewers().filter(r => r.githubName !== prAuthor);

    if (allReviewers.length < count) {
        throw new Error(`리뷰어 수가 부족합니다. (${allReviewers.length}명)`);
    }

    const shuffled = allReviewers.sort(() => Math.random() - 0.5);
    return shuffled.slice(0, count);
}


//3.main 함수
async function main() {
    const pr = github.context.payload.pull_request;
    if (!pr) {
        core.setFailed('이 워크플로우는 pull_request 이벤트에서만 실행됩니다.');
        return;
    }

    const prCreator = pr.user.login;
    const reviewers = selectRandomReviewers(prCreator);

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

