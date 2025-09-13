package com.samsamhajo.deepground.admin.repository;

import java.time.LocalDateTime;

public interface AdminDashboardRepository {
    Long countTotalMembers();
    Long countNewMembersToday(LocalDateTime today);
    Long countTotalPosts();
    Long countTotalStudyGroups();
    Long countTotalReports();
    Long countTodayReports(LocalDateTime today);
    Long countPendingReports();
}
