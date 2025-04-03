package k_webtoons.k_webtoons.model.webtoon;

import java.util.List;

public record WebtoonDetailResponse(
        Long id,
        String titleName,
        String author,
        String thumbnailUrl,
        String synopsis,
        String age,
        String starScore,
        boolean osmuAnime,
        boolean osmuDrama,
        boolean osmuGame,
        boolean osmuMovie,
        boolean osmuOX,
        boolean osmuPlay,
        Boolean finish,
        Boolean isAdult,
        List<String> genre,
        List<String> tag,
        String artistId
) {}
