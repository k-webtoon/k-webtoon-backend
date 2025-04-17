package k_webtoons.k_webtoons.model.admin.status.log_stats_dtos;

public record ClickFlowDto(
    String page,
    String target,
    long count
) {}