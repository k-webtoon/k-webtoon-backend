package k_webtoons.k_webtoons.model.connector;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FlaskLRequest(
        @JsonProperty("webtoon_list") List<Long> webtoonList,
        @JsonProperty("checkbox_state") List<Boolean> checkboxState
) {
}
