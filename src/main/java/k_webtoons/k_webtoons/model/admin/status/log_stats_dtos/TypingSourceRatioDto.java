package k_webtoons.k_webtoons.model.admin.status.log_stats_dtos;

public record TypingSourceRatioDto(
    String source, // e.g., basic, ai
    long count
) {}