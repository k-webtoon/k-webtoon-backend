package k_webtoons.k_webtoons.model.user;

import lombok.Getter;
import java.util.List;

public record RecommendInitRequestDTO(
        List<Long> webtoonIds
) {
}