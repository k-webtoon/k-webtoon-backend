package k_webtoons.k_webtoons.model.webtoon.dto;
import java.util.List;

public record WebtoonPopularityDTO(
    Long webtoonId,
    String thumbnailUrl,
    String titleName,
    String author,
    List<String> genre,
    Boolean adult,
    Boolean finish,
    String starScore,
    Long favoriteCount
) {
}
