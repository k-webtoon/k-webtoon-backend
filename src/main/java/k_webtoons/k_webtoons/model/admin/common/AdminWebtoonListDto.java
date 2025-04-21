package k_webtoons.k_webtoons.model.admin.common;


import k_webtoons.k_webtoons.model.webtoon.Webtoon;

import java.util.ArrayList;
import java.util.List;

public record AdminWebtoonListDto(
        Long id,
        String titleName,
        String author,
        String genre,
        Boolean isPublic,
        String thumbnailUrl,
        String synopsis,
        List<String> tags,
        double totalCount,
        double favoriteCount,
        double collectedNumOfEpi
) {
    public static AdminWebtoonListDto fromEntity(Webtoon w) {
        return new AdminWebtoonListDto(
                w.getId(),
                w.getTitleName(),
                w.getAuthor(),
                (w.getGenre() != null && !w.getGenre().isEmpty()) ? w.getGenre().get(0) : null,
                w.getIsPublic(),
                w.getThumbnailUrl(),
                w.getSynopsis(),
                new ArrayList<>(w.getTags()),
                w.getTotalCount(),
                w.getFavoriteCount(),
                w.getCollectedNumOfEpi()
        );
    }
}

