package k_webtoons.k_webtoons.model.webtoon.dto;

 // 웹툰(좋아요 많은 순) DTO
public record WebtoonPopularityDTO(
    Long webtoonId,
    String titleName,
    String author,
    String thumbnailUrl,
    Long favoriteCount
) {
}
