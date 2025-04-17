package k_webtoons.k_webtoons.model.admin.status.webtoon_stats_dtos;

public record OsmuRatioDto(
    String category, // e.g., movie, drama, game
    long count
) {}