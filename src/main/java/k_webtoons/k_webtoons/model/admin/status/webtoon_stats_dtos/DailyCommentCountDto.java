package k_webtoons.k_webtoons.model.admin.status.webtoon_stats_dtos;

public record DailyCommentCountDto(
    String date,
    long count
) {}