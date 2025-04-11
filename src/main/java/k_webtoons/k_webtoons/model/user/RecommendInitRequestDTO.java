package k_webtoons.k_webtoons.model.user;

import lombok.Getter;
import java.util.List;

@Getter
public class RecommendInitRequestDTO {
    private List<Long> webtoonIds;
}