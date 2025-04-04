package k_webtoons.k_webtoons.model.connector;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ModelLResponse(
        @JsonProperty("seen_webtoon") Long seenWebtoonId,
        @JsonProperty("seen_webtoon_title") String seenWebtoonTitle,
        @JsonProperty("recom_webtoon") Long recomWebtoonId,
        @JsonProperty("recom_webtoon_title") String recomWebtoonTitle,
        @JsonProperty("recom_webtoon_thumbnail_url") String recomWebtoonThumbnailUrl,
        @JsonProperty("cosine_sim") Double cosineSim
) {
}