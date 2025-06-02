// 1. 필요한 import
import * as github from '@actions/github';
import * as core from '@actions/core';
import { getReviewerGroups } from './reviewerUtils'; // 그룹 로딩 함수
import { IReviewer } from './types'; // 타입 정의
import { sendDiscordMessage } from './discord'; // ✅ 디스코드 알림용 함수

const githubClient = github.getOctokit(process.env.GITHUB_TOKEN!);

// 2. 그룹 기반 리뷰어 선택
function selectReviewerByGroup(prAuthor: string): IReviewer {
    const groups = getReviewerGroups();

    const groupA = groups.groupA;
    const groupB = groups.groupB;

    const isAuthorInA = groupA.some(r => r.githubName === prAuthor);
    const targetGroup = isAuthorInA ? groupB : groupA;

    const filtered = targetGroup.filter(r => r.githubName !== prAuthor);

    if (filtered.length === 0) {
        throw new Error(`리뷰어 후보가 없습니다. PR 작성자(${prAuthor}) 외의 사용자가 필요합니다.`);
    }

    // @ts-ignore
    return filtered[Math.floor(Math.random() * filtered.length)];
}
//3.main 함수
async function main() {
    const pr = github.context.payload.pull_request;
    if (!pr) {
        core.setFailed('이 워크플로우는 pull_request 이벤트에서만 실행됩니다.');
        return;
    }

    const prCreator = pr.user.login;
    const selectedReviewer = selectReviewerByGroup(prCreator);

    await githubClient.rest.pulls.requestReviewers({
        owner: github.context.repo.owner,
        repo: github.context.repo.repo,
        pull_number: pr.number,
        reviewers: [selectedReviewer.githubName]
    });

    // ✅ Discord 메시지 전송
    await sendDiscordMessage(selectedReviewer);
}
// 4. 실행
main().catch(err => core.setFailed(err.message));

