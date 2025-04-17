package k_webtoons.k_webtoons.model.admin.status.webtoon_stats_dtos;

public record PublicRatioDto(
    boolean isPublic,
    long count
) {}