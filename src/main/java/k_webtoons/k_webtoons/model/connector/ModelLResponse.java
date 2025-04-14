package k_webtoons.k_webtoons.model.connector;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ModelLResponse(
        @JsonProperty("recom_id") Long recomId,
        @JsonProperty("seen_id") Long seenId,
        @JsonProperty("sim") Integer sim
) {
}