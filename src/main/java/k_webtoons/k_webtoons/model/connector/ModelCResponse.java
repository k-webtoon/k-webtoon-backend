package k_webtoons.k_webtoons.model.connector;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ModelCResponse(
        @JsonProperty("feel Top3") List<String> feelTop3,
        @JsonProperty("message1:") String message1,
        @JsonProperty("message2:") String message2,
        @JsonProperty("message3:") String message3
        ) {
}
