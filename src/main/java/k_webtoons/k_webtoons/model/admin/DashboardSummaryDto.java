package k_webtoons.k_webtoons.model.admin;

public record DashboardSummaryDto(
        long totalUsers,
        long totalWebtoons,
        long totalComments
) {
}