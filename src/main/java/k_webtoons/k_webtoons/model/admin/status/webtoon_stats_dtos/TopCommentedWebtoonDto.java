package k_webtoons.k_webtoons.model.admin.status.webtoon_stats_dtos;

public record TopCommentedWebtoonDto(
    long webtoonId,
    String title,
    long commentCount
) {}