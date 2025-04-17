package k_webtoons.k_webtoons.model.admin.common;

public record UserCountSummaryDTO(
        long total,
        long active,
        long suspended,
        long deactivated
) {
}
