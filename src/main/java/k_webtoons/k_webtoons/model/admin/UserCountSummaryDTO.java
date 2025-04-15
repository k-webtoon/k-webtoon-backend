package k_webtoons.k_webtoons.model.admin;

public record UserCountSummaryDTO(
        long total,
        long active,
        long suspended,
        long deactivated
) {
}
