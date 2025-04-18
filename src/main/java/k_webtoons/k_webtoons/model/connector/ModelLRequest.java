package k_webtoons.k_webtoons.model.connector;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ModelLRequest(
        @JsonProperty("use_popularity") Boolean usePopularity,
        @JsonProperty("use_art_style") Boolean useArtStyle,
        @JsonProperty("use_tags") Boolean useTags
        ){
}
