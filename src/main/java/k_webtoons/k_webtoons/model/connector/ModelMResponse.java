package k_webtoons.k_webtoons.model.connector;

import java.util.List;

public record ModelMResponse(
        List<WebtoonSimilarity> response
) {
}