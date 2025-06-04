import * as fs from 'fs';
import * as yaml from 'yaml';

export interface IReviewer {
    githubName: string;
    discordMention: string;
}

interface ReviewerGroups {
    groupA: IReviewer[];
    groupB: IReviewer[];
}

export function getReviewerGroups(): ReviewerGroups {
    const file = fs.readFileSync('.github/reviewers.yml', 'utf8');
    const parsed = yaml.parse(file);

    const groups = parsed.groups; // ✅ 이 객체에서 groupA, groupB 추출

    const resolveMention = (key: string): string => {
        const envVar = process.env[`DISCORD_MENTION_${key.toUpperCase()}`];
        if (!envVar) throw new Error(`Missing DISCORD_MENTION_${key.toUpperCase()} in environment variables`);
        return envVar;
    };

    const mapGroup = (group: any[]): IReviewer[] =>
        group.map((r) => ({
            githubName: r.githubName,
            discordMention: resolveMention(r.discordKey),
        }));

    return {
        groupA: mapGroup(groups.groupA),
        groupB: mapGroup(groups.groupB),
    };
}

