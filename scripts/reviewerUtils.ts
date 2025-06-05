import * as fs from 'fs';
import * as yaml from 'yaml';
import { IReviewer, ReviewerGroups } from './types';

export function getReviewerGroups(): ReviewerGroups {
    const file = fs.readFileSync('.github/reviewers.yml', 'utf8');
    const parsed = yaml.parse(file);

    console.log('[DEBUG] Parsed YAML:', parsed);

    const groups = parsed.groups; // ✅ 이 객체에서 groupA, groupB 추출

    console.log('[DEBUG] groupA:', groups?.groupA);
    console.log('[DEBUG] groupB:', groups?.groupB);

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

