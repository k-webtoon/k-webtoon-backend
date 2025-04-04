package k_webtoons.k_webtoons.model.connector;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ModelCRequest(
        String comment,
        @JsonProperty("webtoon_idx") Integer webtoonIdx
) {
}
