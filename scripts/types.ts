export interface IReviewer {
    githubName: string;
    discordMention: string;
}
export interface ReviewerGroups {
    groupA: IReviewer[];
    groupB: IReviewer[];
}