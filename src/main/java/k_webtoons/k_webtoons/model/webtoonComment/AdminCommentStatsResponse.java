package k_webtoons.k_webtoons.model.webtoonComment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCommentStatsResponse {

    private Summary summary;
    private List<DailyComment> dailyComments;
    private List<HourlyDistribution> hourlyDistribution;
    private List<TopWebtoonByComment> topWebtoonsByComments;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private long totalComments;
        private double dailyAverage;
        private double averageLength;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyComment {
        private String date;
        private int comments;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HourlyDistribution {
        private String hour;
        private int count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopWebtoonByComment {
        private String name;
        private int comments;
        private double avgLength;
    }
}