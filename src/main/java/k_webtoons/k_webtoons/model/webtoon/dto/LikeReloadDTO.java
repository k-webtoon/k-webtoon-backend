package k_webtoons.k_webtoons.model.webtoon.dto;

public record LikeReloadDTO(
        Long webtoonId,
        Boolean isLiked,
        String titleName,
        String author,
        String thumbnailUrl
) {}