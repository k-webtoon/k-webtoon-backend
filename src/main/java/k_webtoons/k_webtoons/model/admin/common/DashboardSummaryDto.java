package k_webtoons.k_webtoons.model.admin.common;

public record DashboardSummaryDto(
        long totalUsers,
        long totalWebtoons,
        long totalComments
) {
}