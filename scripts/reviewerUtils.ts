import * as fs from 'fs';
import * as yaml from 'yaml';

export interface IReviewer {
    githubName: string;
    slackUserId: string;
}

interface ReviewerGroups {
    groupA: IReviewer[];
    groupB: IReviewer[];
}

export function getReviewerGroups(): ReviewerGroups {
    const file = fs.readFileSync('.github/reviewers.yml', 'utf8');
    const parsed = yaml.parse(file);

    if (!parsed.groups || !parsed.groups.groupA || !parsed.groups.groupB) {
        throw new Error('Invalid reviewers.yml format.');
    }

    return parsed.groups;
}