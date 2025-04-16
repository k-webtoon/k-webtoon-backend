package k_webtoons.k_webtoons.model.admin.status.log_stats_dtos;

public record PageDurationDto(
    String page,
    double avgDuration
) {}