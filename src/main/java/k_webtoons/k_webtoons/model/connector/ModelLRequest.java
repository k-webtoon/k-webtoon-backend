package k_webtoons.k_webtoons.model.connector;

import java.util.List;

public record ModelLRequest(
         List<Long> webtoonList,
         List<Integer> checkboxState) {
}
