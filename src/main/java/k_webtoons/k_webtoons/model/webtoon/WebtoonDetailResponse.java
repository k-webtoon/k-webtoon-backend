package k_webtoons.k_webtoons.model.webtoon;

import java.util.List;

public record WebtoonDetailResponse(
        Long id,
        String titleName,
        String author,
        String url,
        String synopsis,
        String age,
        String starScore,  // ⭐ 소수점 둘째자리 포맷
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
) {
    }
