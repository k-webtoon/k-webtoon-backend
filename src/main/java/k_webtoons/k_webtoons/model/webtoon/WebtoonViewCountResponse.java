package k_webtoons.k_webtoons.model.webtoon;

import java.util.List;

public record WebtoonViewCountResponse(
        Long id,
        Long titleId,
        String titleName,
        String author,
        Boolean adult,
        String age,
        Boolean finish,
        String thumbnailUrl,
        String synopsis,
        List<String> rankGenreTypes,
        Double starScore
) {
}
