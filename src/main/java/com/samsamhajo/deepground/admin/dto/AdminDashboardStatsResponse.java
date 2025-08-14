package com.samsamhajo.deepground.admin.dto;

public record AdminDashboardStatsResponse(
        Long totalMembers,
        Long newMembersToday,
        Long totalPosts,
        Long totalStudyGroups,
        Long totalReports,
        Long todayReports,
        Long pendingReports
) {}
