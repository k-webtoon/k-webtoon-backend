package k_webtoons.k_webtoons.model.admin.common;

public record WebtoonCountSummaryDto(
        long totalWebtoons,
        long publicWebtoons,
        long privateWebtoons
) {
}
