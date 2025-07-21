import * as fs from 'fs';
import * as yaml from 'yaml';
import { IReviewer } from './types';

export function getAllReviewers(): IReviewer[] {
    const file = fs.readFileSync('.github/reviewers.yml', 'utf8');
    const parsed = yaml.parse(file);

    const groups = parsed.groups;

    const resolveMention = (key: string): string => {
        const envVar = process.env[`DISCORD_MENTION_${key.toUpperCase()}`];
        if (!envVar) throw new Error(`Missing DISCORD_MENTION_${key.toUpperCase()}`);
        return envVar;
    };

    const mapGroup = (group: any[]): IReviewer[] =>
        group.map((r) => ({
            githubName: r.githubName,
            discordMention: resolveMention(r.discordKey),
        }));

    const all = [
        ...mapGroup(groups.groupA || []),
        ...mapGroup(groups.groupB || []),
    ];

    return all;
}
