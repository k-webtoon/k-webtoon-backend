package k_webtoons.k_webtoons.model.admin.status.webtoon_stats_dtos;

public record FinishRatioDto(
    String status, // COMPLETED, ONGOING
    long count
) {}